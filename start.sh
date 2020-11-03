ROOT="/home/mimly/booking-spring-boot"
CLIENT="${ROOT}/client"
STATIC="${ROOT}/src/main/resources/static"

cd "$CLIENT" && npm run build &&\
    rm -rf ${STATIC:?}/* &&\
    cp -rf ${CLIENT}/dist/* $STATIC &&\
    sed -E 's/<link[^<>]*?(as="style"|rel="stylesheet")>//g' ${STATIC}/index.html > ${STATIC}/oldschool.html &&\
    git add ${STATIC:?}/* &&\
    cd $ROOT && gradle bootRun
