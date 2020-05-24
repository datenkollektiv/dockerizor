# Dockerize Virgo runtimes

This project is used to create Docker images for [Virgo][Virgo] from [EclipseRT][EclipseRT] with [Dockerizor][Dockerizor].

## How-To Build the Docker Images

Please note: The following step acts on the assumption that you have a Docker daemon running locally.

```bash
./gradlew build dockerize
```

This will create Docker images for `virgo-tomcat-server` and `virgo-jetty-server`.

## How-To Run the Container

To start one of the previously dockerized Virgo runtimes:

```bash
docker run -it --rm --name="virgo-tomcat-server" --publish=8080:8080 -t datenkollektiv/virgo-tomcat-server:3.7.3
```

The command runs the `virgo-tomcat-server` in interactive mode and publishes the ports `8080`.

Please note: The Virgo admin console has been disabled.

## Publish the Container

```bash
$ docker login
$ docker push datenkollektiv/virgo-kernel:3.7.3
$ docker push datenkollektiv/virgo-jetty-server:3.7.3
$ docker push datenkollektiv/virgo-tomcat-server:3.7.3
```

  [Virgo]: http://eclipse.org/virgo
  [EclipseRT]: http://eclipse.org/rt
  [Dockerizor]: https://github.com/datenkollektiv/dockerizor
