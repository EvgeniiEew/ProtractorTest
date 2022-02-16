FROM node:16
RUN apt-get update && \
      apt-get -y install sudo;
RUN apt-get -y install curl

RUN npm install npm@8.1.2 -g
# создание директории приложения
WORKDIR /usr/src/app

# установка зависимостей
# символ астериск ("*") используется для того чтобы по возможности
# скопировать оба файла: package.json и package-lock.json
COPY package*.json ./


RUN npm install -g @angular/cli@13.1.1

RUN npm install
# Если вы создаете сборку для продакшн
# RUN npm ci --only=production

# копируем исходный код
COPY . .

EXPOSE 9000
CMD [ "node", "server.js" ]

