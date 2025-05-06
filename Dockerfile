# Використовуємо офіційний образ AWS Lambda для Java
FROM public.ecr.aws/lambda/java:11

# Копіюємо скомпільований JAR-файл у контейнер
COPY build/libs/telegram_bot_standard-1.0-SNAPSHOT.jar ${LAMBDA_TASK_ROOT}/lib/

# Вказуємо обробник для Lambda
CMD [ "com.example.bot.application.TelegramLambdaHandler::handleRequest" ]

ENTRYPOINT ["top", "-b"]