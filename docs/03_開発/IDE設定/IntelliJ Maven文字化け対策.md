# IntelliJ Maven コンソール文字化け対策

## 概要

IntelliJ の Maven ツールウィンドウから `checkstyle:check` などを実行した際に、
実行結果が文字化けする場合の設定手順を定義する

---

## 対象

- Maven ツールウィンドウから `checkstyle:check` を実行した場合
- Maven ツールウィンドウから `spotbugs:check` を実行した場合
- IntelliJ の Run / Debug ではなく Maven 実行結果だけが文字化けする場合

---

## 原因

IntelliJ の Terminal ではなく、Maven Runner の JVM が使用されるため、
Terminal 側の UTF-8 設定だけでは文字化けが解消しない場合がある

---

## 設定手順

### 1. Maven Runner 設定を開く

以下を開く

```text
File > Settings > Build, Execution, Deployment > Build Tools > Maven > Runner
```

### 2. VM Options に UTF-8 設定を追加する

`VM Options` に以下を設定する

```text
-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
```

### 3. 文字化けが残る場合は言語設定も追加する

Checkstyle などのメッセージを英語固定にしたい場合は、以下も追加する

```text
-Duser.language=en -Duser.country=US
```

設定例:

```text
-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Duser.language=en -Duser.country=US
```

### 4. Maven ツールウィンドウから再実行する

以下のようなコマンドを Maven ツールウィンドウから再実行し、文字化けが解消されたことを確認する

- `checkstyle:check`
- `spotbugs:check`
- `spotless:check`

---

## 補足

- IntelliJ Terminal の UTF-8 設定とは別設定である
- Maven ツールウィンドウ実行時の文字化けは Maven Runner 側を調整する
- `-Duser.language=en -Duser.country=US` を追加すると、非 ASCII の診断文を避けやすい
