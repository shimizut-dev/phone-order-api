# package-structure.md

## 1. 目的

本ドキュメントは `phone-order-api` の Java パッケージ構成方針を定義する。

---

## 2. 基本方針

- パッケージはレイヤ単位を基本とする
- MVPでは過度な分割を避ける
- ドメイン中心で意味のある配置にする
- 後から分割しやすい構成にする

---

## 3. ルートパッケージ

```text
jp.co.example.phoneorderapi
```

※ 実際の organization に応じて変更する。

---

## 4. 推奨パッケージ構成

```text
jp.co.example.phoneorderapi
├─ presentation
│  ├─ controller
│  ├─ request
│  ├─ response
│  └─ handler
│
├─ application
│  ├─ usecase
│  │  ├─ order
│  │  └─ delivery
│  ├─ service
│  ├─ dto
│  └─ mapper
│
├─ domain
│  ├─ model
│  │  ├─ order
│  │  ├─ party
│  │  ├─ delivery
│  │  ├─ line
│  │  ├─ sim
│  │  ├─ phone
│  │  └─ accessory
│  ├─ service
│  ├─ repository
│  └─ exception
│
└─ infrastructure
├─ persistence
│  ├─ entity
│  ├─ repository
│  └─ mapper
└─ config
```

---

## 5. domain 配下の考え方

### 5.1 order

- Order 
- OrderId 
- OrderStatus

### 5.2 party

- OrderParty
-  OrderPartyId
-  OrderPartyRole
-  PersonName
-  PersonNameKana
-  Gender
-  BirthDate
-  Address

### 5.3 delivery

- Delivery
- DeliveryId
- DeliveryLine
- DeliveryLineId
- DeliveryStatus
- DeliveryMethod
- DeliveryTimeSlot
- DeliveryQuantity
- RequestedDeliveryAt

### 5.4 line

- Line
- LineId
- LineContractKind
- MSISDN

### 5.5 sim

- Sim
- SimId
- SimForm
- SimStatus

### 5.6 phone

- Phone
- PhoneId
- IMEI

### 5.7 accessory

- Accessory
- AccessoryId

---

## 6. usecase 配下の考え方

application/usecase/order

- CreateOrderUseCase
- GetOrderUseCase
- CancelOrderUseCase

application/usecase/delivery

- CreateDeliveryUseCase
- GetDeliveryUseCase

---

## 7. infrastructure 配下の考え方

- DB entity は domain object と分離する
- repository 実装は infrastructure に配置する
- JPA を使う場合も domain には持ち込まない

---

## 8. 補足

MVP時点ではシンプルさを優先する。  
将来的に機能が増えた場合は bounded context や feature 単位の再編成を検討する。

---
