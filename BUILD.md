## How-To build dockerizor

We use [Gradle](http://gradle.org/) to build the Gradle Plugin [dockerizor](https://github.com/eclipsesource/dockerizor).

```bash
$ git clone https://github.com/eclipsesource/dockerizor.git
$ cd dockerizor
$ ./gradlew build
```

Publish the plugin to your local [Maven](https://maven.apache.org/) repository.

```bash
$ ./gradlew clean build publishToMavenLocal
```

Publish the plugin to [Gradle Plugins](https://plugins.gradle.org/)

```bash
$ ./gradlew clean build publishPlugins
```
