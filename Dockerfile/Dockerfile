FROM terdong/playframework-preparation
MAINTAINER DongHee Kim <terdong@gmail.com>

##origin########################################################
ENV PROJECT_HOME /usr/src
WORKDIR $PROJECT_HOME
ENV GITHUB_PROJECT_NAME car_adverts_service
ENV GITHUB_URL https://github.com/terdong/${GITHUB_PROJECT_NAME}
RUN git clone -b production --single-branch $GITHUB_URL
WORKDIR $GITHUB_PROJECT_NAME
RUN sbt stage
#################################################################

ENV PROJECT_STAGE_PATH server/target/universal/stage/bin
WORKDIR $PROJECT_STAGE_PATH

ENV EXECUTE_NAME server
ENV PARAM_PORT 9000
ENV PARAM_SECRET_KEY please_change_me
ENV PARAM_DB_HOSTNAME localhost
ENV PARAM_DB_PORT 8000
ENV PARAM_HOST_ALLOWED .

CMD ./$EXECUTE_NAME -Dplay.http.secret.key=${PARAM_SECRET_KEY} \
-Dplay.filters.hosts.allowed.0=${PARAM_HOST_ALLOWED} \
-Ddynamodb.endpoint=http://${PARAM_DB_HOSTNAME}:${PARAM_DB_PORT} \
-Dhttp.port=${PARAM_PORT}

EXPOSE 9000