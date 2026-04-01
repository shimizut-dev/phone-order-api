# 開発環境（Development Environment）

## 📌 ローカルフォルダ構成

以下の構成を標準とする

```
C:\dev
 ├─docs
 ├─tools
 ├─github
 ├─sandbox
 └─temp
```

---

## 📌 ディレクトリ用途

### docs

開発ドキュメント・設計資料などを配置

例：

```
C:\dev\docs
 ├─architecture
 ├─database
 ├─api
 └─notes
```

---

### tools

開発補助ツールを配置

例：

- A5:SQL Mk-2
- その他開発補助ツール

```
C:\dev\tools
```

---

### github

GitHubリポジトリを配置

例：

```
C:\dev\github
 └─phone-order-api
```

---

### sandbox

検証用ディレクトリ

用途：

- 動作確認
- サンプル作成
- 技術検証

```
C:\dev\sandbox
```

---

### temp

一時ファイル配置

用途：

- 一時ダウンロード
- 作業中ファイル
- 検証ファイル

```
C:\dev\temp
```

---

## 📌 ツール配置ルール

### システムツール

デフォルトインストール

例：

- Java
- Git
- Docker
- IntelliJ IDEA

---

### 開発補助ツール

以下に配置

```
C:\dev\tools
```

---

## 📌 GitHubクローン先

```
C:\dev\github
```

例：

```
git clone https://github.com/shimizut-dev/phone-order-api.git
```

---

## 📌 運用方針

- 開発関連は C:\dev 配下に統一
- 開発補助ツールは tools に配置
- GitHub は github に配置
- 検証は sandbox を使用
- 一時ファイルは temp を使用

---

## 📌 目的

- 開発環境の統一
- 再現性の向上
- 環境構築の簡略化
- テックリード型開発の実現
