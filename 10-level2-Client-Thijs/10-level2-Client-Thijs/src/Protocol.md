# Protocol specificatie level 1

The protocol is described below. The protocol describes the following scenarios:

- Setting up a connection between client and server.
- Broadcasting a message to all connected clients.
- Periodically sending heartbeat to connected clients.
- Disconnection from the server.
- Handling invalid messages.

In the description below, _C -\> S_ represents a message from the client C is send to server S. When applicable, C is extended with a number to indicate a specific client, e.g., C1, C2, etc. The keyword "_others"_ is used to indicate all other clients except for the client who made the request.

Messages can contain a JSON body. Text between "\<" and "\>" are placeholders.

1.
## Establishing a connection

The client first sets up a socket connection to which the server responds with a welcome message. The client supplies a username on which the server responds with an OK if the username is accepted or an ERROR with a number in case of an error.

_Note_: A username may only consist of characters, numbers, and underscores ('\_') and has a length between 3 to 14 characters.

**Happy flow:**

Client sets up the connection with server.

S -\> C: WELCOME {"msg": "\<welcome message\>"}

\<welcome message\> is the welcome message the server sends to the client. The server can decide on its contents.

After a while when the client logs the user in:

C -\> S: LOGIN {"username":"\<username\>"}

S -\> C: LOGIN\_RESP {"status":"OK"}

\<username\> is the username of the user that needs to be logged in.

To other clients (Only applicable when working on Level 2):

S -\> _others_: JOINED {"username":"\<username\>"}

**Error messages:**

S -\> C: LOGIN\_RESP {"status":"ERROR", "code":\<error code\>}

Possible \<error code\> on LOGIN\_RESP:

| **Error code** | **Description** |
| --- | --- |
| 5000 | User already logged in |
| 5001 | Username has an invalid format or length |
| 5002 | User cannot login twice |

1.
## Broadcast message

Sends a message from a client to all other clients. The sending client does not receive the message himself but gets a confirmation that the message has been sent.

**Happy flow:**

C -\> S: BROADCAST\_REQ {"message":"\<message\>"}

S -\> C: BROADCAST\_RESP {"status":"OK"}

\<message\> is the message that must be send.

Other clients receive the message as follows:

S -\> _others_: BROADCAST {"username":"\<username\>","message":"\<message\>"}

\<username\>: is the username of the user that is sending the message.

**Error messages:**

S -\> C: BROADCAST\_RESP {"status": "ERROR", "code": \<error code\>}

Possible \<error code\>:

| **Error code** | **Description** |
| --- | --- |
| 6000 | User is not logged in |

1.
## Heartbeat message

Sends a ping message to the client to check whether the client is still active. The receiving client should respond with a pong message to confirm it is still active. If after 3 seconds no pong message has been received by the server, the connection to the client is closed. Before closing, the client is notified with a DSCN message, with reason code 7000. The DSCN is also used if the message is too long to be parsed (reason code 7001). Servers should be capable of parsing messages at least 1024 bytes long.

The server sends a ping message to a client every 10 seconds. The first ping message is send to the client 10 seconds after the client is logged in.

When the server receives a PONG message while it is not expecting one, a PONG\_ERROR message will be returned.

**Happy flow:**

S -\> C: PING

C -\> S: PONG

**Error messages:**

S -\> C: DSCN {"reason": \<reason code\>}

[Server disconnects the client]

Possible \<reason code\>:

| **Reason code** | **Description** |
| --- | --- |
| 7000 | Pong timeout |
| 7001 | Unterminated message |

S -\> C: PONG\_ERROR {"code": \<error code\>}

Possible \<error code\>:

| **Error code** | **Description** |
| --- | --- |
| 8000 | Pong without ping |

1.
## Termination of the connection

When the connection needs to be terminated, the client sends a bye message. This will be answered (with a BYE\_RESP message) after which the server will close the socket connection.

**Happy flow:**

C -\> S: BYE

S -\> C: BYE\_RESP {"status":"OK"}

[Server closes the socket connection]

Other, still connected clients, clients receive:

S -\> _others_: LEFT {"username":"\<username\>"}

**Error messages:** _None_

1.
## Invalid message header

If the client sends an invalid message header (not defined above), the server replies with an unknown command message. The client remains connected.

**Example flow:**

C -\> S: MSG This is an invalid message

S -\> C: UNKNOWN\_COMMAND

1.
## Invalid message body

If the client sends a valid message, but the body is not valid JSON, the server replies with a pars error message. The client remains connected.

**Example flow:**

C -\> S: BROADCAST\_REQ {"aaaa}

S -\> C: PARSE\_ERROR

# Protocol specification level 2:

1.
## List of connected users

Allows clients to request a list of all currently connected users.

**Happy Flow:**

Client requests the user list:

C -\> S: USER\_LIST\_REQ

Server responds with the list of users:

S -\> C: USER\_LIST\_RESP {"users":["\<username1\>", "\<username2\>", ...]}

1.
## Private messaging

Clients can send private messages to specific users.

**Happy Flow:**

Client sends a private message request to a specific user:

C -\> S: PRIVATE\_MSG\_REQ {"receiver":"\<username\>", "message":"\<message\>"}

Server acknowledges the request:

S -\> C: PRIVATE\_MSG\_RESP {"status":"OK"}

Server forwards the message to the intended receiver:

S -\> Receiver: PRIVATE\_MSG {"sender":"\<username\>", "message":"\<message\>"}

**Bad flow:**

S -\> C: PRIVATE\_MSG\_RESP {"status":"ERROR", "code":\<error code\>}

Possible Error messages:

| **Error code** | **Description** |
| --- | --- |
| 6100 | Target client not found |
| 6101 | User is not logged in |
| 6102 | Message body empty |

1.
## Multiplayer Guessing game

A multiplayer game where users guess a number. The game begins on request and ends when all participants have guessed correctly or time runs out.

**Happy flow:**

A user requests to start a game:

C -\> S: GAME\_START\_REQ

Server acknowledges and sends an invitation to other users:

S -\> C: GAME\_START\_RESP {"status":"OK}

S -\> Others: GAME\_INVITE {"creator":"\<username\>"}

Users join the game:

C -\> S: GAME\_JOIN\_REQ

S -\> C: GAME\_JOIN\_RESP {"status":"OK"}

Server starts the game:

S -\> Players: GAME\_BEGIN

Players guess the number:

C -\> S: GAME\_GUESS\_REQ {"number":\<number\>}

S -\> C: GAME\_GUESS\_RESP {"feedback":"Too Low/Too High/Correct"}

Game ends with results:

S -\> Players: GAME\_END {"results":[

{"username":"\<username\>", "time":\<timeInMs\>, "status":"Winner/Loser"},

...

]}

**Bad flow:**

A user requests to start a game:

S -\> C: GAME\_START\_RESP {"status":"ERROR", "code":\<error code\>}

Possible Error messages:

| **Error code** | **Description** |
| --- | --- |
| 6200 | Not enough players |
| 6201 | Game already in progress |

Users join the game:

S -\> C: GAME\_JOIN\_RESP {"status":"ERROR", "code":\<error code\>}

Possible Error messages:

| **Error code** | **Description** |
| --- | --- |
| 6300 | No game in progress |
| 6301 | Game has started |

**Error messages:**

S -\> C: GAME\_GUESS\_RESP {"status":"ERROR", "code":\<error code\>}

Possible Error messages:

| **Error code** | **Description** |
| --- | --- |
| 6400 | User has not joined the game in progress |
| 6401 | No game in progess |
| 6402 | User has already guessed correctly |

# Foutcodes