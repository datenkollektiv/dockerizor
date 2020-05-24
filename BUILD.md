# How-To build dockerizor

> Note: This is a fork from [eclipsesource/dockerizor](https://github.com/eclipsesource/dockerizor)

We use [Gradle](http://gradle.org/) to build the Gradle Plugin [dockerizor](https://github.com/datenkollektiv/dockerizor).

```bash
$ git clone https://github.com/datenkollektiv/dockerizor.git
$ cd dockerizor
$ ./gradlew build
```

## Publish the plugin to your local [Maven](https://maven.apache.org/) repository.

```bash
./gradlew clean build publishToMavenLocal
```

## Publish the plugin to [Gradle Plugins](https://plugins.gradle.org/)

Use the "Plugin Portal tasks":

* `login` - Update the gradle.properties files so this machine can publish to the Gradle Plugin portal.
* `publishPlugins` - Publishes this plugin to the Gradle Plugin portal.

```bash
./gradlew clean build publishPlugins
```
