package com.eclipsesource.dockerizor

class DockerizorExtension {

    String maintainer
    String description

    String uri
    String repository
    String tag

    boolean dryRun
    boolean noCache

    boolean createLocalCopy

    String javaImage
    String virgoVersion
    String ciJobName
    String ciJobNumber
    String virgoFlavour
    String virgoHome
    String hostname
    Boolean removeAdminConsole
    Boolean removeSplash
    Boolean enableUserRegionOsgiConsole
    Boolean exposeHttpPort

    String[] pickupFiles
    String[] binFiles

    String entryPoint

    Closure postDockerizeHook = {
    }

    def postProcessor(DockerizorTask task) {
        postDockerizeHook(task)
    }

    String getShortName() {
        switch (virgoFlavour) {
            case 'VJS':
                return "jetty-server"
            case 'VTS':
                return "tomcat-server"
            case 'VRS':
                return "nano"
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }

    String getArchiveName() {
        switch (virgoFlavour) {
            case 'VJS':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-jetty-server-latest"
                    default:
                    return "virgo-jetty-server-${virgoVersion}"
                }
            case 'VTS':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-tomcat-server-latest"
                    default:
                    return "virgo-tomcat-server-${virgoVersion}"
                }
            case 'VRS':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-nano-rap-latest"
                    default:
                    throw new IllegalArgumentException("Virgo flavour ${virgoFlavour}/${virgoVersion} *not* supported")
                }
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }

    String getDownloadUrl() {
        switch (virgoFlavour) {
            case 'VJS':
            case 'VTS':
                switch (virgoVersion) {
                    case 'latest':
                    case ~/.*D-\d{14}/: 
                        if(!ciJobNumber) {
                            ciJobNumber='lastSuccessfulBuild'
                        }
                        return "https://ci.eclipse.org/virgo/job/${ciJobName}/${ciJobNumber}/artifact/packaging/${shortName}/build/distributions/${archiveName}.zip"
                    case ~/.*M\d{2}/:
                    return "http://www.eclipse.org/downloads/download.php?file=/virgo/milestone/${virgoFlavour}/${archiveName}.zip&mirror_id=580&r=1"
                    default:
                    return "http://www.eclipse.org/downloads/download.php?file=/virgo/release/VP/${virgoVersion}/${archiveName}.zip&mirror_id=580&r=1"
                }
            case 'VRS':
                switch (virgoVersion) {
                    case 'latest':
                    return "https://ci.eclipse.org/virgo/job/${ciJobName}/lastSuccessfulBuild/artifact/packaging/${shortName}/build/distributions/${archiveName}.zip"
                    default:
                    throw new IllegalArgumentException("Virgo flavour ${virgoFlavour}/${virgoVersion} *not* supported")
                }
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }
}
