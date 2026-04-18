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
jp.co.shimizutdev.phoneorderapi
```

現行実装では上記をルートパッケージとする。

---

## 4. 推奨パッケージ構成

```text
jp.co.shimizutdev.phoneorderapi
├─ presentation
│  ├─ generated
│  │  ├─ api
│  │  └─ model
│  ├─ order
│  ├─ exception
│  └─ log
│
├─ application
│  └─ order
│
├─ domain
│  └─ order
│
└─ infrastructure
   ├─ codegen
   ├─ config
   ├─ log
   ├─ persistence
   │  └─ order
   └─ repository
      └─ order
```

`presentation.generated` 配下は `docs/05_api/openapi.yaml` から `openapi-generator-maven-plugin` が生成する。
生成物の出力先は `target/generated-sources/openapi` であり、手動編集しない。

---

## 5. domain 配下の考え方

### 5.1 order

- Order
- OrderId
- OrderStatus

### 5.2 party

- OrderParty
- OrderPartyId
- OrderPartyRole
- PersonName
- PersonNameKana
- Gender
- BirthDate
- Address

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
