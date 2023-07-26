FROM node:14-alpine AS builder

WORKDIR /opt/application
COPY . .

RUN yarn && yarn build:prod

FROM nginx
COPY --from=builder /opt/application/dist /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]
