[![Last Commit](https://img.shields.io/github/last-commit/datenkollektiv/dockerizor?style=flat)](https://github.com/datenkollektiv/dockerizor/commits/)
[![Build Status](https://circleci.com/gh/datenkollektiv/dockerizor.svg?style=shield)](https://circleci.com/gh/datenkollektiv/dockerizor)

# Eclipse Virgo dockerizor

> Note: This is a fork from [eclipsesource/dockerizor](https://github.com/eclipsesource/dockerizor)

A [Gradle][gradle] plug-in to create a Docker image that includes an [Eclipse Virgo][Virgo] container.

During our preparations for the EclipseCon talk about our first Docker project, we spent quite some time
packaging Virgo containers inside Docker images.

We investigated how to improve the continuous delivery of Virgo powered applications using Gradle and Docker. The outcome is the Gradle Plugin ''dockerizor''.

In a first step we automated the generation of basic Virgo images.

### How-To use the Gradle Plugin

The following build snippet applies the [Gradle Plugin Dockerizor][dockerizor] to your build script:

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.de.datenkollektiv.dockerizor:dockerizor:0.9.4"
  }
}

apply plugin: "de.datenkollektiv.dockerizor"
```

### Building basic Virgo images

Both the Docker...

```groovy
dockerizor {
  maintainer = 'Florian Waibel <fwaibel@datenkollektiv.de>'
  description = 'Docker image build with dockerizor'

  // docker client configuration
  uri = "http://localhost:4243"
  tag = "latest"

  javaImage = 'java:8u92-jre-alpine'
}
```

...and Virgo specific configuration is within a `dockerizor` block.

```groovy
dockerizor {
  virgoFlavour = 'VTS'
  removeAdminConsole = true
  removeSplash = true

  imageName = 'virgo-tomcat-server'
}
```

The snippet above creates a Docker image named 'virgo-tomcat-server' with the Virgo flavor VTS (Virgo Server for Apache Tomcat).

```bash
$ docker images | grep virgo-tomcat-server
datenkollektiv/virgo-tomcat-server            3.7.3                 a95cb6faa18f        2 minutes ago       175MB
```

The generated basic images for Virgo are available via [Docker Hub][dockerhub]:

 * Virgo Server for Apache Tomcat: https://registry.hub.docker.com/r/datenkollektiv/virgo-tomcat-server/
 * Virgo Jetty Server: https://registry.hub.docker.com/r/datenkollektiv/virgo-jetty-server/
 * Virgo Kernel: https://registry.hub.docker.com/r/datenkollektiv/virgo-kernel/

[Virgo]: http://www.eclipse.org/virgo/ "Virgo"
[dockerhub]: https://hub.docker.com/ "Docker Hub"
[gradle]: http://gradle.org/ "Gradle"
[dockerizor]: https://plugins.gradle.org/plugin/de.datenkollektiv.dockerizor "Gradle Plugin Dockerizor"
