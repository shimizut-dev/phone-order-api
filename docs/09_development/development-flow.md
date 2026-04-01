# 開発フロー

本プロジェクトの開発フローを定義する。

---

# ■ 概要

本プロジェクトでは以下の開発スタイルを採用する

- Issue駆動開発
- AI活用
- GitHub Flow

---

# ■ 開発スタイル

## Issue駆動開発

Issue を起点として開発を進める

```
Issue
 ↓
Branch
 ↓
実装
 ↓
PR
 ↓
Merge
```

### メリット

- 作業単位が明確になる
- 進捗管理が容易になる
- 履歴が分かりやすい
- レビューしやすい

---

## AI活用

AI（ChatGPT） を活用する

### 活用内容

- Issue 作成
- PR 作成
- レビュー

### メリット

- 開発効率向上
- 品質向上
- 作業時間短縮

---

## GitHub Flow

GitHub Flow に準拠した開発

本プロジェクトでは以下の2つのブランチを使用する

- main: 常に動く状態を保つメインブランチ
- feature: 新機能やバグ修正などの作業ブランチ

```
feature
↓
PR
↓
main
```

### メリット

- シンプルな運用
- 小さな変更単位
- 安全なリリース
- レビューしやすい

---

# ■ Issue と PR の関係

Issue と PR の各項目が対になるように起票する

## Issue と PR の対応表

| Issue | PR |
|------|----|
| 概要 | 概要 |
| 対応内容 | 対応内容 |
| 完了条件 | 完了条件 |

## テンプレート

- Issueテンプレート  
  .github/ISSUE_TEMPLATE/task.md

- PRテンプレート  
  .github/pull_request_template.md


---

# ■ Squash Merge

## 概要

複数のコミットを1つのコミットにまとめてマージする方式

## メリット

- コミット履歴がきれいになる
- 変更内容が分かりやすい
- revertしやすい
- レビュー単位が明確になる

---

# ■ 前提

- 1 Issue = 1 Branch
- 1 Issue に対して PR は 1つ以上作成してもよい  
  ※1 Issue で 1 PR を作成して Issue をクローズできるのが理想だが、  
  1つ目の PR では対応が不足する場合があるため、  
  追加の PR を作成するケースを許容する
- 1 Issue 内で複数コミット可
- PRレビュー後に main へマージ
- main ブランチは常にデプロイ可能な状態を維持する
- Squash merge を使用

---

# ■ 開発フロー

## 開発フロー概要

```
Issue作成
 ↓
ブランチ作成
 ↓
実装
 ↓
コミット
 ↓
Push
 ↓
PR作成
 ↓
レビュー
 ↓
マージ
 ↓
Issueクローズ
 ↓
featureブランチ削除
```

## ① Issue作成

AI（ChatGPT） を使用して Issue を作成する

---

## ② ブランチ作成

ブランチ命名規則は以下を参照

docs/09_development/git-branch-naming.md

```bash
git checkout -b feature/<issue-number>
```

例

```bash
git checkout -b feature/12
```

---

## ③ 実装

- 実装
- 動作確認
- テスト

以下を参照

docs/09_development/chatgpt-thread-guidelines.md

---

## ④ コミット

コミットメッセージ規則は以下を参照

docs/09_development/git-commit-message.md

```bash
git add .
git commit -m <type>([optional scope]): <description> (#<issue_number>)
```

例

```bash
git add .
git commit -m "feat(order): 注文作成機能を追加 (#12)"
```

---

## ⑤ Push

```bash
git push origin feature/<issue-number>
```

例

```bash
git push origin feature/12
```

---

## ⑥ PR作成

AI（ChatGPT） を使用して PR を作成
PRのタイトルはコミットメッセージと同じフォーマット

---

## ⑦ レビュー

- 自己レビュー
- AI（ChatGPT）レビュー（任意）

---

## ⑧ マージ

- Squash merge
- main へマージ

---

## ⑨ Issueクローズ

- PRマージ後にIssueをクローズする
- 完了条件を満たしていることを確認する

---

## ⑩ featureブランチ削除

- Issueクローズ時にリモート/ローカルのfeatureブランチ削除する
