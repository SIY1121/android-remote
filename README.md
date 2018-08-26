# android-remote
Androidを自動化するアプリのPCサイドアプリの作りかけ、多分続きは作らない？

# 技術的情報

以下を参照ください

[Androidの画面をPCにミラーリングするソフトを作る１](https://qiita.com/siy1121/items/71fadad0c3a1aa085867)

[Androidの画面をPCにミラーリングするソフトを作る２　リアルタイムタッチ編](https://qiita.com/siy1121/items/9e96cb1eb14d9090a2b6)

## 実装済み

- 画面のミラーリング
- javascriptによる簡易自動化システム

以下のような感じで動きます

```javascript
app.tap(x,y);
app.swipe(x1,y1,x2,y2);
```
## 未実装
- リアルタイムタッチ

# 動作環境

Java8で動作確認済み

# 実行方法

### step1-1　Intellij IDEA でプロジェクトを開き、Javaアプリケーションとして実行

or 

### step1-2　cliでプロジェクトフォルダに移動後、以下を実行
```shell
$ gradlew jfxRun
```

### step2
Android側のアプリ（記事参照）を立ち上げStart後、このアプリのメインメニューの一番上のボタンを押す

# その他
質問等があればTwitter @SIY1121 へどうぞ
