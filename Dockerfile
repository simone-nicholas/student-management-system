FROM ubuntu:latest
LABEL authors="waldren"

ENTRYPOINT ["top", "-b"]