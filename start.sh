ROOT="/home/mimly/booking-spring-boot"
CLIENT="${ROOT}/client"
STATIC="${ROOT}/src/main/resources/static"

cd "$CLIENT" && npm run build &&\
    rm -rf ${STATIC:?}/* &&\
    cp -rf ${CLIENT}/dist/* $STATIC &&\
    git add ${STATIC:?}/* &&\
    cd $ROOT && gradle bootRun
