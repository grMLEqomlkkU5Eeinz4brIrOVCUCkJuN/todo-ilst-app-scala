# Todo Web App
A simple todo list app that uses vue, thorium, slick and scala. Just an interesting stack I am playing around with

I had to dig a little into armeria to figure out cors, but otherwise thorium was nice to work with

## Running it

The easiest way is with Docker Compose. You'll need a `.env` file with your image details:

```env
DOCKERHUB_USERNAME=your-username
DOCKERHUB_REPO_NAME=your-repo
```

Then just:

```bash
docker compose up
```

Frontend is on [http://localhost](http://localhost), backend on [http://localhost:8080](http://localhost:8080).

If you'd rather run it locally, you'll need Java 17+, sbt, Node 22+, and a MariaDB instance running on port 3306 with a database called `db`. Then in two terminals:

```bash
# terminal 1
cd todo-backend && sbt run

# terminal 2
cd todo-frontend && npm install && npm run dev
```
