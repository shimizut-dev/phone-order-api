# order-line-creation-rules.md

## ドキュメント区分

補助ドキュメント  
本ドキュメントは設計補助・実装方針整理を目的とする。  
正式な仕様判断は正式ドキュメントを参照すること。

---

## 1. 目的

本ドキュメントは `phone-order-api` における `OrderLine` の生成ルールを定義する。  
`OrderLine` は注文の明細を表す重要なドメイン要素であり、不正状態を生成時点で防ぐことを目的とする。

---

## 2. 基本方針

- `OrderLine` は明細種別に応じて必要な要素のみを保持する
- `OrderLine` の不正状態は生成時点で防ぐ
- 不正な組み合わせを持つ `OrderLine` は生成してはならない
- 明細全体を見ないと判断できないルールは `Order` 集約で保証する
- `OrderLine` 単体で判断できる整合性は `OrderLine` 自身が保証する

---

## 3. 明細種別

- 回線
- 移動機
- アクセサリ

---

## 4. 生成ルール

### 4.1 共通ルール

- `OrderLineId` は必須
- `OrderLineKind` は必須
- `OrderQuantity` は必須
- `OrderQuantity` は1以上
- `OrderLine` は明細種別に応じて必要な要素のみを保持する

### 4.2 回線注文

必須

- `Line`
- `Line.Sim`

禁止

- `Phone`

### 4.3 移動機注文

必須

- `Phone`

禁止

- `Line`
- `Accessory`

### 4.4 アクセサリ注文

必須

- `Accessory`

禁止

- `Line`
- `Phone`

---

## 5. 推奨生成方法

`OrderLine` は汎用コンストラクタで直接生成せず、明細種別ごとの生成メソッドを通して生成する。

- `createLineOrder(...)`
- `createPhoneOrder(...)`
- `createAccessoryOrder(...)`

---

## 6. 実装方針

- `OrderLine` のコンストラクタは `private` または `package-private`
- 外部から直接不正な `OrderLine` を生成できないようにする
- 生成時のバリデーションエラーはドメイン例外として扱う
- 不正状態を作成後に検出するのではなく、作成時点で拒否する
