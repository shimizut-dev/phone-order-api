
# 📝 コミットメッセージ規則

## 準拠規約

本プロジェクトでは **Conventional Commits** に準拠します

https://www.conventionalcommits.org/en/v1.0.0/

---

## ルール

```
<type>([optional scope]): <description> (#<issue_number>)
```

---

## type

| type | 説明 |
|------|------|
| feat | 機能追加 |
| fix | バグ修正 |
| refactor | リファクタリング（仕様変更なし） |
| docs | ドキュメント修正 |
| test | テスト追加・修正 |
| chore | ビルド・設定・その他変更 |

---

## scope

任意

- 各機能名

例

```
order
delivery
payment
```

---

## description

コミットの説明

- 命令形で記載
- 簡潔に記載

例

```
注文作成機能を追加
配送先バリデーションを修正
Order集約の責務を整理
```

---

## issue_number

対象のissue番号

---

## 例

```
feat(order): 注文作成機能を追加 (#12)
docs(docs): アーキテクチャドキュメント追加 (#13)
refactor(order): Order集約の責務を整理 (#14)
fix(order): 注文作成時のバリデーション修正 (#15)
test(order): 注文作成テストを追加 (#16)
chore(build): Gradle設定を変更 (#17)
```
