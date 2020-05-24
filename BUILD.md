## How-To build dockerizor

> Note: This is a fork from [eclipsesource/dockerizor](https://github.com/eclipsesource/dockerizor)

We use [Gradle](http://gradle.org/) to build the Gradle Plugin [dockerizor](https://github.com/datenkollektiv/dockerizor).

```bash
$ git clone https://github.com/datenkollektiv/dockerizor.git
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
