.PHONY: build up down logs clean rebuild restart

build:
	docker-compose build

up:
	docker-compose up -d

down:
	docker-compose down

logs:
	docker-compose logs -f

clean:
	docker-compose down -v

rebuild:
	docker-compose down
	docker-compose build --no-cache
	docker-compose up -d

restart:
	docker-compose restart
