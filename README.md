# Mutual Authentication Protocol
**Software Implementation of the Mutual Authentication Protocol Without the Participation of a Trusted Authority**

This software implements the software mutual authentication protocol using the RSA cryptographic algorithm based on Java BigInteger methods and cryptographically secure RNG.  
This library allows two parties to generate a common secret (usually a symmetric key for message encryption during chat session)

Kotlin targeting Java (9.0)

## How to use this software

#### Node creation
This software relies on Node.kt and NodeSettings.kt classes.  
Node.kt is an entry point for all authentication checks and session key generation.  
If you plan to use the library for the first time, then:   
1. Create an *RSA* instance and generate new RSA Key Pair using *RSA.generateKeyPair()*. Keys are 2048 bits long.  
2. Create a *NodeSettings* instance using *NodeSettingsFactory.createNew(id: Int, Pair<KeyRSA, KeyRSA>)*. Provide Id and Generated RSA Key Pair (or your own).  
**ID collisions are not handled in any way, this depends heavily on context and environment. You are welcome to implement your own ID generator**  
3. Create a *Node* instance with *NodeSettings* as an argument.
4. Create as many *Nodes* as you want that way.

If you have already created at least one *NodeSettings* instance, then you can recreate the *NodeSettings* in order to recreate the *Node* instance with the same ID and RSA keys.  
To do this, create a *NodeSettings* instance using *NodeSettingsFactory.getFromFile(path: String)*. Specify the path file.

#### Authentication, key distribution
Authentication is possible using the *Node.connectToNode(node: Node)* method.   
- Returns true if mutual authentication between _this_ and the other *Node* instance is complete.  
- This method also registers both *nodes* when invoked on an unknown Node.  
- If a *Node* instance with the same ID and different RSA Key Pair tries to authenticated itself, false will be returned and no values will be overriden.  


## Future plans
[ ] Implement AES for message encryption and decryption using the common secret.  
[ ] Modify the protocol to work over HTTP connection.  
[ ] Implement EC cryptography instead of RSA.  