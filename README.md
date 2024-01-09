# Gomoku-Finalproject
Gomoku-Finalproject 是112智慧型行動裝置軟體設計課程設計的期末專案自己設計的五子棋小遊戲

因為檔案太大，所以全部檔案是上傳到 Releases 的 Hostedfile1.0。

第一頁功能為選擇遊戲模式，共有三種，雙人對決、簡單 AI、困難 AI。

AI 的做法為利用 alpha-beta pruning 去計算棋盤當前落點最佳步數，

困難 AI 是考慮到深度為3，簡單為2，也就是模擬到未來幾步後的棋盤情況。

第二頁功能為回到上一頁重新選擇難度和依照當下模式重新開始對局，

勝利失敗有 textview 顯示及音效改變。

alpha-beta pruning 演算法在 startplay.java 中撰寫，

其中計算當前得分是以活三、活四、活五的數量以及所有棋子距離盤面中心遠近進行權重加總計算，

並未考慮連線是否連接到邊界或其他五子棋中較優勢的步數計算，這是未來可以改進的點。

demo.mp4 是全部功能的展示影片

# Demo

![image](https://github.com/CHANGCHINGCHUAN/Gomoku-Finalproject/blob/main/demo.gif)
