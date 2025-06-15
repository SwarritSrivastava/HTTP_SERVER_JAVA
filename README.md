# 🧠 Simple Java HTTP Server

This is a multithreaded Java HTTP server that can handle basic HTTP `GET` and `POST` requests. It's a minimal implementation to serve plain text, read/write files, and optionally include basic `Content-Encoding` headers.

---

## 📦 Features

- ✅ Handles `GET` and `POST` requests  
- 📁 File reading and writing via `/files/{filename}`  
- 🧪 Echo endpoint via `/echo/{message}`  
- 🧑‍💻 User-agent inspection via `/user-agent`  
- 🧵 Multithreaded: each client handled in a separate thread  
- 🧲 Recognizes common encodings (`gzip`, `deflate`, `br`)  
- 🔁 Listens on port `4221`

---

## 🔧 How to Run

Make sure you have Java installed. Then:

```bash
javac Main.java
java Main
```

The server will run at:

```
http://localhost:4221
```

---

## 🌐 Supported Endpoints

### `GET /`
Returns a basic 200 OK response:

```
HTTP/1.1 200 OK
```

---

### `GET /echo/{message}`
Echoes back `{message}` with a `Content-Length` header.

```bash
curl http://localhost:4221/echo/Hello
```

---

### `GET /user-agent`
Returns the `User-Agent` string of the client:

```bash
curl -A "my-custom-agent" http://localhost:4221/user-agent
```

---

### `GET /files/{filename}`
Returns the contents of the file if it exists. Responds with 404 otherwise.

```bash
curl http://localhost:4221/files/myfile.txt
```

---

### `POST /files/{filename}`
Writes the body of the request to a file.

#### Example (Linux/Mac):
```bash
curl -X POST --data-binary "Hello World" http://localhost:4221/files/newfile.txt
```

#### Example (PowerShell):
```powershell
curl -X POST --data-binary $'Line1\nLine2' http://localhost:4221/files/newfile.txt
```

---

## 📌 Content-Encoding Notes

If the client sends `Accept-Encoding: gzip`, `deflate`, or `br`, the server includes the `Content-Encoding` header in the response.  
**⚠️ Actual encoding is not implemented yet.**

---

## 🧪 Example Workflow

```bash
# Write to file
curl -X POST --data-binary "Test content." http://localhost:4221/files/test.txt

# Read from file
curl http://localhost:4221/files/test.txt
```

---

## ⚠️ Limitations

- ❌ Files saved in current working directory only
- ❌ No HTTPS or persistent connections
- ❌ No MIME type detection
- ❌ No actual content compression
- ❌ Basic header parsing (no case-insensitivity or multiline support)

---

## 🚀 Improvements To Consider

- Implement gzip/deflate compression
- Add persistent connection support
- MIME type detection for files
- Proper full header parsing
- Serve static HTML or JSON
---

## 👨‍💻 Author

Built with Anger By Swarit Srivastava

Feel free to fork and improve!
