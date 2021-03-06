package com.eclipsesource.dockerizor

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.BuildImageCmd
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.command.BuildImageResultCallback
import com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DockerizorTask extends DefaultTask {

    @OutputDirectory
    File outputDir

    private Dockerfile dockerfile = new Dockerfile()

    private boolean dryRun
    private boolean noCache

    private boolean createLocalCopy

    protected String uri
    private String repository
    private String tag

    DockerizorTask() {
        outputDir = new File(project.buildDir, "dockerizor")
    }

    @TaskAction
    void build() {
        uri = project.dockerizor.uri
        repository = project.dockerizor.repository
        tag = project.dockerizor.tag

        dryRun = project.dockerizor.dryRun
        noCache = project.dockerizor.noCache

        createLocalCopy = project.dockerizor.createLocalCopy

        outputDir.mkdirs()
        dockerfile.init(new File(outputDir, "Dockerfile"))
        dockerfile.write()
        File dockerFileOrFolder = dockerfile.dockerfile

        logger.info("Dockerfile '${dockerfile.dockerfile}' created:")
        logger.info("--- snip ---")
        dockerfile.dockerfile.eachLine { line -> logger.info line }
        logger.info("--- snap ---")

        if (dryRun) {
            logger.warn("This is a dry-run. No Docker image will be created (and pushed)!")
        } else {
            logger.info("Creating image with repository:tag '${repository}:${tag}'...")
            def configBuilder = new DefaultDockerClientConfig.Builder().withDockerHost(uri)
            DockerClient dockerClient = DockerClientBuilder.getInstance(configBuilder).withDockerCmdExecFactory(new DockerCmdExecFactoryImpl()).build()
            BuildImageCmd buildImageCmd = dockerClient.buildImageCmd(dockerFileOrFolder)
            buildImageCmd.withTag(repository + ':' + tag)
            buildImageCmd.withRemove(true)
            buildImageCmd.withNoCache(noCache)
            String imageId = buildImageCmd.exec(new BuildImageResultCallback()).awaitImageId()
            logger.info("Created image [${imageId}]")

            createLocalCopyIfRequested(dockerClient)
        }
    }

    private createLocalCopyIfRequested(DockerClient dockerClient) {
        if (!createLocalCopy) {
            logger.debug("Skipping creation of local copy.")
            return
        }
        def timestamp = new Date().format('yyyyMMddHHmmss', TimeZone.getTimeZone('GMT'))
        logger.info("Creating temporary container to create a copy of the custom Virgo runtime...")
        CreateContainerResponse container = dockerClient.createContainerCmd("${repository}:${tag}")
                .withName("dockerizor_tmp_" + timestamp).exec()

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.id, project.dockerizor.virgoHome).exec()
        if (!response.available()) {
            logger.error("Failed to create local copy of custom Virgo container!")
        } else {
            File localVirgoCopy = new File(project.buildDir, "virgo-" + project.name + "-" + timestamp + ".tar")
            logger.info("Creating local copy of custom Virgo container {}", localVirgoCopy)
            OutputStream fos = new FileOutputStream(localVirgoCopy)
            fos << response
            fos.close()
        }

        dockerClient.removeContainerCmd(container.id).exec()
        logger.info("Removed temporary container '{}'.", container.id)
    }

    void 'FROM'(String imageName) {
        logger.info "Building image FROM ${imageName}"
        dockerfile.addCommand('FROM ' + imageName)
    }

    void 'RUN'(String command) {
        dockerfile.addCommand('RUN ' + command)
    }

    void 'ADD_ENTRYPOINT'(String destination) {
        InputStream resourceStream = this.class.getResourceAsStream("/entrypoint.sh")
        byte[] entrypointContent = new byte[resourceStream.available()]
        resourceStream.read(entrypointContent)

        Path entrypointScript = Paths.get(outputDir.absolutePath, "entrypoint.sh");
        Files.write(entrypointScript, entrypointContent);
        dockerfile.addCommand("ADD entrypoint.sh ${destination}")
    }

    void 'ADD'(String fileName, String destination) {
        'ADD' (new File(fileName), destination)
    }

    void 'ADD'(File file, String destination) {
        if (file.isDirectory()) {
            throw new UnsupportedOperationException("Directories are not supported (yet).")
        }
        logger.info "ADDing ${file.name} to Docker image at ${destination}"
        logger.info "Copying ${file} side-by-side to Dockerimage: ${outputDir}."
        project.copy {
            from file
            into outputDir
        }
        dockerfile.addCommand("ADD ${file.name} ${destination}")
    }

    void 'ENV' (String key, String value) {
        logger.info "Setting ENVironment ${key}=${value}"
        dockerfile.addCommand("ENV ${key} ${value}")
    }

    void 'EXPOSE' (long port) {
        logger.info "About to EXPOSE port ${port}"
        dockerfile.addCommand("EXPOSE ${port}")
    }

    void 'ENTRYPOINT' (String command) {
        dockerfile.addCommand("ENTRYPOINT [\"${command}\"]")
    }

    void 'CMD' (String command) {
        dockerfile.addCommand("CMD [\"${command}\"]")
    }

    void 'USER' (String user) {
        logger.info "Switching to USER ${user}"
        dockerfile.addCommand("USER ${user}")
    }

    void 'MAINTAINER' (String maintainer) {
        logger.info "Adding MAINTAINER ${maintainer}"
        dockerfile.addCommand("MAINTAINER ${maintainer}")
    }

    void 'LABEL' (String label) {
        logger.info "Adding LABEL '${label}'"
        dockerfile.addCommand("LABEL ${label}")
    }
}
