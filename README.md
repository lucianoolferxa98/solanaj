# solanaj

Based on [p2p.org/solanaj](https://github.com/p2p-org/solanaj)

Solana blockchain client, written in pure Java.
Solanaj is an API for integrating with Solana blockchain using the [Solana RPC API](https://docs.solana.com/apps/jsonrpc-api)

## Requirements
- Java 8+

## Dependencies
- bitcoinj
- OkHttp
- Moshi

### Example

##### Generate Keypair

```java
Account account = new Account();
byte[] privateKey = account.getSecretKey();
PublicKey publicKey = account.getPublicKey();
String address = publicKey.toBase58();
```
##### Transfer lamports
```java
RpcClient client = new RpcClient(Cluster.TESTNET);

PublicKey fromPublicKey = new PublicKey("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo");
PublicKey toPublickKey = new PublicKey("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5");
int lamports = 3000;

Account signer = new Account(secret_key);

Transaction transaction = new Transaction();
transaction.addInstruction(SystemProgram.transfer(fromPublicKey, toPublickKey, lamports));

String signature = client.getApi().sendTransaction(transaction, signer);
```

##### Get balance

```java
RpcClient client = new RpcClient(Cluster.TESTNET);

long balance = client.getApi().getBalance(new PublicKey("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo"));
```
##### Create nonce account
```java
Account walletAccount = new Account();
String walletAddress = walletAccount.getPublicKey().toBase58();

Account nonceAccount = new Account();
String nonceAddress = nonceAccount.getPublicKey().toBase58();
Transaction transaction = new Transaction();

RpcClient rpcClient = new RpcClient(Cluster.DEVNET);
long rent = rpcClient.getApi().getMinimumBalanceForRentExemption(NonceAccount.NONCE_ACCOUNT_LENGTH);
transaction.addInstruction(SystemProgram.createAccount(walletAddress, nonceAddress, rent, NonceAccount.NONCE_ACCOUNT_LENGTH, SystemProgram.PROGRAM_ID));
transaction.addInstruction(SystemProgram.nonceInitialize(nonceAddress, walletAddress));

String recentBlockhash = rpcClient.getApi().getRecentBlockhash();
transaction.setRecentBlockHash(recentBlockhash);
transaction.setFeePayer(walletAddress);

transaction.sign(Arrays.asList(walletAccount, nonceAccount));
String txHash = rpcClient.getApi().sendTransaction(transaction);
```
##### Use nonceAccount transfer 
```java
Transaction transaction = new Transaction();
transaction.addInstruction(SystemProgram.nonceAdvance(nonceAddress,walletAddress));
transaction.addInstruction(SystemProgram.transfer(walletAddress,"receiveAddress",1000000000L));

AccountInfo nonceAccountInfo = rpcClient.getApi().getAccountInfo(nonceAccount.getPublicKey());
List<String> data = nonceAccountInfo.getValue().getData();
NonceAccount nonce = NonceAccount.fromAccountData(data);

transaction.setRecentBlockHash(nonce.getBlockHash());
transaction.setFeePayer(walletAddress);
//sender and nonceAccount`s owner signature
transaction.sign(walletAccount);
String txHash = rpcClient.getApi().sendTransaction(transaction);
```

##### Calculate Associated token Address
```java
String associatedTokenAddress = TokenProgram.getAssociatedTokenAddress(TokenProgram.TOKEN_PROGRAM_ID, mintAddress, walletAddress);
```
##### Create Associated token Address
```java
String toAssociatedTokenAddress = TokenProgram.getAssociatedTokenAddress(TokenProgram.TOKEN_PROGRAM_ID, mint, to);
transaction.addInstruction(TokenProgram.createAssociatedTokenAccount(TokenProgram.TOKEN_PROGRAM_ID,mint,toAssociatedTokenAddress,to,from));
```

##### Transfer SPL token
```java
String fromAssociatedTokenAddress = TokenProgram.getAssociatedTokenAddress(TokenProgram.TOKEN_PROGRAM_ID, mint, from);
String toAssociatedTokenAddress = TokenProgram.getAssociatedTokenAddress(TokenProgram.TOKEN_PROGRAM_ID, mint, to);
transaction.addInstruction(TokenProgram.createTransferChecked(TokenProgram.TOKEN_PROGRAM_ID,fromAssociatedTokenAddress,mint,toAssociatedTokenAddress,from,new ArrayList<>(),value,decimals));
```

##### Serializable transaction
```java
Message message = new Message();

//add a few Instruction
message.addInstruction();
message.addInstruction();
message.addInstruction();

message.setRecentBlockHash(blockHash);
message.setFeePayer(feePayer);

byte[] serializedMessage = message.serialize();

//sign
List<byte[]> signers = new ArrayList<>();
List<byte[]> signatures = new ArrayList<>();
signers.add(new byte[]{});//signer A
signers.add(new byte[]{});//signer B
for (byte[] privateKey : signers) {
    TweetNaclFast.Signature signatureProvider = new TweetNaclFast.Signature(new byte[0],privateKey);
    byte[] signature = signatureProvider.detached(serializedMessage);
    signatures.add(signature);
}

byte[] signedTx = SerializeUtils.serialize(serializedMessage, signatures);

String base64SignedTx = Base64.getEncoder().encodeToString(signedTx);
String txHash = Base58.encode(signatures.get(0));
```

## Contribution

Welcome to contribute, feel free to change and open a PR.


## License

Solanaj is available under the MIT license. See the LICENSE file for more info.
