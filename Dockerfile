# Використовуємо офіційний образ AWS Lambda для Java
FROM public.ecr.aws/lambda/java:21

# Копіюємо скомпільований JAR-файл у контейнер
COPY build/libs/telegram-bot-standard-1.0-SNAPSHOT.jar ${LAMBDA_TASK_ROOT}/lib/

# Вказуємо обробник для Lambda
CMD [ "com.example.bot.application.TelegramLambdaHandler::handleRequest" ]

ENTRYPOINT ["top", "-b"]