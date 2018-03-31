# Mutual Authentication Protocol
**Software Implementation of the Mutual Authentication Protocol Without the Participation of a Trusted Authority**

This software implements the software mutual authentication protocol using the RSA cryptographic algorithm based on Java BigInteger methods and cryptographically secure RNG.  
This library allows two parties to generate a common secret (AES-128 key for message encryption during chat session)

Kotlin targeting JVM (JDK 9.0)

## How to use this software

### Authentication API Usage
This software relies on Node.kt class.  
Node.kt is an entry point for all authentication checks and session key generation.  
If you plan to use the library, then:  
1. Generate RSA key pair using *generateKeyPair* method from RSA class.
2. Use NodeFileRepository to add new nodes via *addNode* method.  
3. Use NodeFileRepository to read nodes from files via *readNode* method.  
4. Use *connectToNode()* from Node class to perform mutual authentication and generate common AES-128 key.
5. Use *sendMessage()* from Node class to send messages encrypted with common AES-128 key OR use the AES-128 key in your code to encrypt stuff.  
For quick test of this software launch an app.kt file located in src/auth.

### Sample App Usage  
A demo app showcases authentication and message sending features in a more user-friendly way using JavaFX.
- List of nodes is loaded from secret_data directory on program launch.
- Use *Create Node* to create new node with specified Node ID.
- Use *Update From Files* to read node info from files. This will override all already loaded nodes.
- Select First and Second Nodes, then use *Connect Nodes* to perform mutual authentication between selected Nodes and share a common AES-128 key.
- Select First and Second Nodes, then use *Send Message* to send random message encrypted with common AES-128 key.
- Use *Hard Reset* to erase all nodes and existing node files.



## Development targets
[x] FileNodeRepository to work with File System.
[x] Implement AES for message encryption and decryption using the common secret.  
[ ] Modify the protocol to work over HTTP connection.
[ ] Modify the protocol to work over TCP/IP sockets.
[ ] Explore and implement elliptic curves cryptography.