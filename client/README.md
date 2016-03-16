
## 環境

サーバー Redshift（Public dc1.large）
クライアント Note PC（Ubuntu）
ネットワーク：クライアントからS3の間で、1~3MB/sくらい

## １ノード

### 10ファイル1thread

size：96.8 MB
count：10
Thread:1

create jsons
→14298 milliseconds
upload jsons
→186452 milliseconds

### 10ファイル10thread(10 connection)

size：96.8 MB
count：10
Thread:10

create jsons
→14298 milliseconds
upload jsons
→131659 milliseconds

### １ファイル1thread

size：987.8 MB
count：1
Thread:1

create jsons
→18425 milliseconds
upload jsons
→150868 milliseconds


## 5ノード

### 10ファイル1thread

size：96.8 MB
count：10
Thread:1

create jsons
→14298 milliseconds
upload jsons
→208777 milliseconds
→192585 milliseconds

### 10ファイル10thread(10 connection)

size：96.8 MB
count：10
Thread:10

create jsons→14298 milliseconds
upload jsons
→131780 milliseconds
→126852 milliseconds

### １ファイル1thread

size：987.8 MB
count：1
Thread:1

create jsons→18425 milliseconds
upload jsons
→188091 milliseconds
→152528 milliseconds