## Dockerize Virgo runtimes

This project is used to create Docker images for [Virgo][Virgo] from [EclipseRT][EclipseRT] with [Dockerizor][Dockerizor].

### Build the Docker Images

Please note: The following step acts on the assumption that you have a Docker daemon running locally.

```sh
$ ./gradlew build dockerize
```

This will create Docker images for `virgo-tomcat-server` and `virgo-jetty-server`.

### Running the Container

To start one of the previously dockerized Virgo runtimes:

```sh
$ docker run -it --rm --name="virgo-tomcat-server" --publish=8080:8080 -t eclipsesource/virgo-tomcat-server:3.7.0.M03
```

The command runs the ``virgo-tomcat-server`` in interactive mode and publishes the ports ``8080``.

Please note: The Virgo admin console has been disabled.

### Publish the Container

  [Virgo]: http://eclipse.org/virgo
  [EclipseRT]: http://eclipse.org/rt
  [Dockerizor]: https://github.com/eclipsesource/dockerizor
