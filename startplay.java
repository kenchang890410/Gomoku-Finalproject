package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class startplay extends AppCompatActivity {

    private Button back, Restart;

    private GameAdapter gameAdapter;

    private int gamemode = 0, playingsong = 0;

    private TextView playerTurn, Startgame;

    private boolean player = false, gamestate = true;

    int[][] initialChessBoard = new int[8][8];

    private MediaPlayer mediaPlayer, mediaPlayer_win, mediaPlayer_loss;

    private GridView gridView;

    private int MAX_DEPTH = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        back = findViewById(R.id.back);
        gridView = findViewById(R.id.gridview);
        playerTurn = findViewById(R.id.playerturn);
        Restart = findViewById(R.id.restart);
        Startgame = findViewById(R.id.startgame);

        playerTurn.setText("black turn");

        //Setting music
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        mediaPlayer_win = MediaPlayer.create(this, R.raw.winmusic);
        mediaPlayer_loss = MediaPlayer.create(this, R.raw.lossmusic);

        mediaPlayer.setLooping(true);

        mediaPlayer.start();
        mediaPlayer_win.start();
        mediaPlayer_loss.start();
        mediaPlayer_win.pause();
        mediaPlayer_loss.pause();

        //back to title button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //restartGame button
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        //Initialize GameAdapter
        gameAdapter = new GameAdapter(this, initialChessBoard);
        gridView.setAdapter(gameAdapter);


        //get game mode
        Intent intent = getIntent();
        if (intent != null) {
            int myVariable = intent.getIntExtra("myVariable", 0);
            if(myVariable == 0)
                Startgame.setText("PVP Black First Move");
            else if(myVariable == 2)
                Startgame.setText("PVE Easy Mode");
            else
                Startgame.setText("PVE Hard Mode");
            gamemode = myVariable;
            MAX_DEPTH = myVariable;

        }

        //Setting button color
        Restart.setBackgroundColor(Color.parseColor("#DEB878"));
        back.setBackgroundColor(Color.parseColor("#DEB878"));

        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private class GameAdapter extends BaseAdapter {
        private Context context;
        private int[][] chessBoard;

        public GameAdapter(Context context, int[][] chessBoard) {
            this.context = context;
            this.chessBoard = chessBoard;
        }

        @Override
        public int getCount() {
            return 64;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(140, 150));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(25, 20, 25, 20);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(gamestate){

                            int row = position / 8;
                            int col = position % 8;

                            if(chessBoard[col][row] == 0){
                                if(gamemode < 1) {//PVP mode
                                    if(player){
                                        chessBoard[col][row] = 2;
                                        playerTurn.setText("black turn");
                                        if(checkWinner(chessBoard) == 2){
                                            playerTurn.setText("white win");
                                            playingsong = 2;
                                            mediaPlayer.pause();
                                            mediaPlayer_loss.start();
                                            gamestate = false;
                                        }

                                    }
                                    else{
                                        chessBoard[col][row] = 1;
                                        playerTurn.setText("white turn");
                                        if(checkWinner(chessBoard) == 1){
                                            playerTurn.setText("black win");
                                            playingsong = 1;
                                            mediaPlayer.pause();
                                            mediaPlayer_win.start();
                                            gamestate = false;
                                        }
                                    }
                                    player = !player;
                                }
                                else{//PVE mode
                                    chessBoard[col][row] = 1;
                                    if(checkWinner(chessBoard) == 1){
                                        playerTurn.setText("black win");
                                        playingsong = 1;
                                        mediaPlayer.pause();
                                        mediaPlayer_win.start();
                                        gamestate = false;
                                    }
                                    else{
                                        int[] bestMove = findBestMove(chessBoard, 2);
                                        chessBoard[bestMove[0]][bestMove[1]] = 2;
                                        if(checkWinner(chessBoard) == 2) {
                                            gamestate = false;
                                            playerTurn.setText("white win");
                                            playingsong = 2;
                                            mediaPlayer.pause();
                                            mediaPlayer_loss.start();
                                        }
                                    }

                                }
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
            }
            else {
                imageView = (ImageView) convertView;
            }

            int row = position / 8;
            int col = position % 8;
            int chessPiece = chessBoard[col][row];

            if (chessPiece == 0) {
                imageView.setImageResource(R.drawable.nochess);
            }
            else if (chessPiece == 1) {
                imageView.setImageResource(R.drawable.blackchess);
            }
            else if (chessPiece == 2) {
                imageView.setImageResource(R.drawable.whitechess);
            }

            return imageView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaPlayer_loss != null) {
            mediaPlayer_loss.release();
            mediaPlayer_loss = null;
        }

        if (mediaPlayer_win != null) {
            mediaPlayer_win.release();
            mediaPlayer_win = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        mediaPlayer_win.pause();
        mediaPlayer_loss.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (playingsong == 0) {
            mediaPlayer.start();
        }
        else if (playingsong == 1) {
            mediaPlayer_win.start();
        }
        else {
            mediaPlayer_loss.start();
        }
    }

    private void restartGame() {
        for (int i = 0; i < initialChessBoard.length; i++) {
            for (int j = 0; j < initialChessBoard[i].length; j++) {
                initialChessBoard[i][j] = 0;
            }
        }

        gameAdapter = new GameAdapter(this, initialChessBoard);
        gridView.setAdapter(gameAdapter);

        playerTurn.setText("black turn");
        Toast.makeText(this, "game restart", Toast.LENGTH_SHORT).show();
        gamestate = true;
        player = false;

        mediaPlayer_win.seekTo(0);
        mediaPlayer_loss.seekTo(0);
        mediaPlayer.seekTo(0);

        mediaPlayer_win.pause();
        mediaPlayer_loss.pause();
        mediaPlayer.start();


    }

    public int[] findBestMove(int[][] board, int player) {
        int[] result = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = player;
                    int score = alphaBeta(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board[i][j] = 0;

                    if (score > bestScore) {
                        bestScore = score;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result;
    }

    private int alphaBeta(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || checkWinner(board) > 0) {
            return evaluate(board);
        }

        int player = maximizingPlayer ? 2 : 1;

        int bestValue = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = player;
                    int value = alphaBeta(board, depth - 1, alpha, beta, !maximizingPlayer);
                    board[i][j] = 0;

                    if (maximizingPlayer) {
                        bestValue = Math.max(bestValue, value);
                        alpha = Math.max(alpha, bestValue);
                    } else {
                        bestValue = Math.min(bestValue, value);
                        beta = Math.min(beta, bestValue);
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return bestValue;
    }

    //return winner
    private int checkWinner(int[][] chessBoard) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {

                if (chessBoard[i][j] == chessBoard[i][j + 1] &&
                        chessBoard[i][j] == chessBoard[i][j + 2] &&
                        chessBoard[i][j] == chessBoard[i][j + 3] &&
                        chessBoard[i][j] == chessBoard[i][j + 4] &&
                        chessBoard[i][j] != 0) {
                    return chessBoard[i][j];
                }

                if (chessBoard[j][i] == chessBoard[j + 1][i] &&
                        chessBoard[j][i] == chessBoard[j + 2][i] &&
                        chessBoard[j][i] == chessBoard[j + 3][i] &&
                        chessBoard[j][i] == chessBoard[j + 4][i] &&
                        chessBoard[j][i] != 0) {
                    return chessBoard[j][i];
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (chessBoard[i][j] == chessBoard[i + 1][j + 1] &&
                        chessBoard[i][j] == chessBoard[i + 2][j + 2] &&
                        chessBoard[i][j] == chessBoard[i + 3][j + 3] &&
                        chessBoard[i][j] == chessBoard[i + 4][j + 4] &&
                        chessBoard[i][j] != 0) {
                    return chessBoard[i][j];
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 4; j < 8; j++) {
                if (chessBoard[i][j] == chessBoard[i + 1][j - 1] &&
                        chessBoard[i][j] == chessBoard[i + 2][j - 2] &&
                        chessBoard[i][j] == chessBoard[i + 3][j - 3] &&
                        chessBoard[i][j] == chessBoard[i + 4][j - 4] &&
                        chessBoard[i][j] != 0) {
                    return chessBoard[i][j];
                }
            }
        }

        return 0;
    }

    private int evaluate(int[][] board) {

        int aiScore = calculateScore(board, 2);
        int opponentScore = calculateScore(board, 1);

        return aiScore - opponentScore * 2;
    }

    private int calculateScore(int[][] board, int player) {
        int score = 0;

        score += calculateDirectionScore(board, player, 0, 1);
        score += calculateDirectionScore(board, player, 1, 0);
        score += calculateDirectionScore(board, player, 1, 1);
        score += calculateDirectionScore(board, player, 1, -1);

        return score;
    }

    private int calculateDirectionScore(int[][] board, int player, int rowDirection, int colDirection) {
        int score = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player) {
                    int consecutiveCount = 1;

                    for (int k = 1; k <= 4; k++) {
                        int newRow = i + k * rowDirection;
                        int newCol = j + k * colDirection;

                        if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[i].length) {
                            if (board[newRow][newCol] == player) {
                                consecutiveCount++;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    if (consecutiveCount == 3) {
                        score += 10;
                    } else if (consecutiveCount == 4) {
                        score += 10000;
                    } else if (consecutiveCount == 5) {
                        score += 1000000;
                    }

                    int centerDistance = Math.abs(i - board.length / 2) + Math.abs(j - board[i].length / 2);
                    score -= centerDistance;
                }
            }
        }

        return score;
    }
}
