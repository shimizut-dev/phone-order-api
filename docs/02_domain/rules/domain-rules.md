# domain-rules.md

## 1. 目的

本ドキュメントは `phone-order-api` におけるドメインルールを定義する。  
ドメインモデル図 (`domain-model.drawio`) では表現しきれないドメイン制約を明文化する。

---

## 2. 対象

- Order
- OrderParty
- OrderLine
- Line
- Sim
- Phone
- Accessory

---

## 3. 基本方針

- Order は集約ルートとする
- OrderLine は注文の明細を表す
- OrderParty は注文関係者を表す
- OrderLine の整合性は生成時に保証する
- 配送制約の詳細は `delivery-rules.md` を参照する

---

## 4. 注文ルール

### 4.1 注文成立条件

- 1注文は1件以上の注文関係者を持つ
- 1注文は1件以上の注文明細を持つ

---

## 5. 注文関係者ルール

### 5.1 注文関係者役割

- 注文者
- 契約者
- 利用者
- 請求者

### 5.2 注文関係者制約

- 同一人物が複数役割を兼務できる

---

## 6. 注文明細ルール

### 6.1 明細種別

- 回線
- 移動機
- アクセサリ

### 6.2 回線注文

必須

- Line
- Sim

禁止

- Phone

### 6.3 移動機注文

必須

- Phone

禁止

- Line
- Accessory

### 6.4 アクセサリ注文

必須

- Accessory

禁止

- Line
- Phone

---

## 7. 回線ルール

### 7.1 回線

回線は以下を持つ

- 回線ID
- MSISDN
- 回線契約種別
- SIM

---

## 8. 未確定情報

- MSISDN は未確定の場合がある
- IMEI は未確定の場合がある
