# IntelliJ XML DTD 設定手順

`checkstyle.xml` などの XML ファイルで `URI が未登録です` の警告が出る場合は、IntelliJ に DTD を登録する

---

## 対象例

- `config/checkstyle/checkstyle.xml`

次のような警告が出る場合を対象とする

- `URI が未登録です (設定 | 言語 & フレームワーク | スキーマと DTD)`

---

## 設定手順

1. `File > Settings > Languages & Frameworks > Schemas and DTDs` を開く
2. `+` を押して新しい設定を追加する
3. 次を入力する

### Checkstyle DTD の設定例

- URI
  - `https://checkstyle.org/dtds/configuration_1_3.dtd`
- Location
  - ローカルに保存した `configuration_1_3.dtd` を指定する

1. 設定を保存する
2. `checkstyle.xml` を開き直して警告が消えることを確認する

---

## 運用方針

- リポジトリには DTD ファイルを持ち込まない
- `checkstyle.xml` は標準の外部 DTD 参照のままにする
- IntelliJ 側のローカル設定で DTD を解決する

---

## 補足

- この設定は IDE の XML ハイライト表示向けであり、Maven の `checkstyle:check` の実行には影響しない
- 別の XML で同様の警告が出た場合も、同じ画面から URI とローカルファイルの対応を追加する
