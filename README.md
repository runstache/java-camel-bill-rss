# java-camel-bill-rss
RSS Feed Reader for pulling legislative information from the State Web Site.

## Building the Application

The application is bundled with a docker file that will build the application in layers.  The below command will build the Docker image:

```bash
docker buildx build --platform=linux/arm64 --tag rss-reader:latest --build-arg MAVEN_USER="github-user" --build-arg MAVEN_PASSWORD="git-hub-key" --build-arg MAVEN_CLI_OPTS="--settings .m2/settings.xml --batch-mode" --build-arg MAVEN_OPTS="-Dmaven.repo.local=.m2/repository" .
```
