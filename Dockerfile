FROM gradle:7.6-jdk17-alpine as builder
WORKDIR /build

# 저사양(1GB) 인스턴스에서 빌드해도 OOM 나지 않도록 Gradle 메모리 제한 + 데몬 비활성화
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx512m -Dorg.gradle.daemon=false"

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 빌더 이미지에서 애플리케이션 빌드
COPY . /build

# config-repo 서브모듈의 application.yml 을 클래스패스로 복사.
# application.yml 은 .gitignore 대상이라 fresh clone 시 config-repo 에서만 존재함.
# 둘 다 없으면 명확한 안내와 함께 빌드를 중단한다.
RUN if [ -f config-repo/application.yml ]; then \
        cp config-repo/application.yml src/main/resources/application.yml; \
    fi; \
    test -f src/main/resources/application.yml || { \
        echo "ERROR: src/main/resources/application.yml 이(가) 없습니다."; \
        echo "       'git submodule update --init --recursive' 후 다시 빌드하세요."; \
        exit 1; \
    }

RUN gradle build -x test --parallel

# APP
FROM openjdk:17.0.2-slim
WORKDIR /app

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8081

# JVM 힙 상한 등 런타임 옵션 (docker-compose 의 JAVA_OPTS 로 주입, 기본값은 아래)
# AWS 프리티어 1GB 기준. 더 큰 인스턴스면 .env 의 JAVA_OPTS 로 올리면 됨.
ENV JAVA_OPTS="-Xms128m -Xmx320m"

# root 대신 nobody 권한으로 실행
USER nobody

# sh -c + exec 로 실행해 JAVA_OPTS 를 확장하면서 java 가 PID 1 이 되도록 함(정상 종료 시그널 처리)
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dsun.net.inetaddr.ttl=0 -jar app.jar"]
