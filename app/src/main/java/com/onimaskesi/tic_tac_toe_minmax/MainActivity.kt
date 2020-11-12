package com.onimaskesi.tic_tac_toe_minmax

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {

    lateinit var currentPosition : Position
    lateinit var elements : Elements
    var USER : Char = '\u0000'
    var COMPUTER : Char = '\u0000'
    lateinit var userName : String
    var computerScore = 0
    var userScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = intent.getCharExtra("user",'X')
        userName = intent.getStringExtra("userName").toString()

        userNameTV.text = userName.toUpperCase()

        startTheGame(user)

        //elements = Elements()
        //positionTest("OOXX O  X")
        //positionTest("O   O   X")


    }

    fun createGamers(user : Char){

        elements = Elements(user)
        //elements = Elements('♦','♥', '☺')
        USER = elements.USER
        COMPUTER = elements.COMPUTER

        userSign.text = "(${USER})"
        computerSign.text = "(${COMPUTER})"

    }

    fun startTheGame(user : Char){

        createGamers(user)
        currentPosition = Position(elements)
        if(user == 'O'){
            //makeMove(COMPUTER)
            currentPosition.positionArray = "X        ".toCharArray()
        }
        fillGame(currentPosition.positionArray)

    }

    fun button1click(view : View){
        buttonClick(button1 , 0)
    }
    fun button2click(view : View){
        buttonClick(button2, 1)
    }
    fun button3click(view : View){
        buttonClick(button3, 2)
    }
    fun button4click(view : View){
        buttonClick(button4, 3)
    }
    fun button5click(view : View){
        buttonClick(button5, 4)
    }
    fun button6click(view : View){
        buttonClick(button6, 5)
    }
    fun button7click(view : View){
        buttonClick(button7 , 6)
    }
    fun button8click(view : View){
        buttonClick(button8, 7)
    }
    fun button9click(view : View){
        buttonClick(button9, 8)
    }

    fun buttonClick(button : Button, index : Int){

        if(currentPosition.positionArray[index] == elements.EMPTY){

            currentPosition.positionArray[index] = USER

            createnewPosition(currentPosition)

            if(isThereAnyEmptySpace()){
                makeMove(COMPUTER)
            }

        } else {

            button.isClickable = false

        }
    }

    fun playAgainClick(view : View){
        textView.text = ""
        finishGame()
        if(USER == 'O'){
            USER = 'X'
        } else {
            elements.USER = 'O'
            USER = elements.USER
        }
        startTheGame(USER)
    }

    fun fillGame(array : CharArray){

        button1.text = array[0].toString()
        button1.setTextColor(Color.parseColor("#ffffff"))
        button2.text = array[1].toString()
        button2.setTextColor(Color.parseColor("#ffffff"))
        button3.text = array[2].toString()
        button3.setTextColor(Color.parseColor("#ffffff"))
        button4.text = array[3].toString()
        button4.setTextColor(Color.parseColor("#ffffff"))
        button5.text = array[4].toString()
        button5.setTextColor(Color.parseColor("#ffffff"))
        button6.text = array[5].toString()
        button6.setTextColor(Color.parseColor("#ffffff"))
        button7.text = array[6].toString()
        button7.setTextColor(Color.parseColor("#ffffff"))
        button8.text = array[7].toString()
        button8.setTextColor(Color.parseColor("#ffffff"))
        button9.text = array[8].toString()
        button9.setTextColor(Color.parseColor("#ffffff"))


    }

    fun createnewPosition(newPosition: Position){

        fillGame(newPosition.positionArray)
        newPosition.childArray.clear()
        newPosition.parent = Position(elements)
        newPosition.score = 0
        newPosition.whoesTour = null
        currentPosition = newPosition

        /*
        if(isGameFin() != 7){
            finishGame()
        }
         */
        isGameFin()

    }

    fun scoreUpdate(score : Int){

        if(score == 1){
            computerScore += 1
            computerScoreTV.text = computerScore.toString()
        }
        if(score == -1){
            userScore += 1
            userScoreTV.text = userScore.toString()
        }
    }

    fun isGameFin() {

        giveScoreThePosition(currentPosition)

        val score = currentPosition.score

        when (score) {
            1 -> textView.text = "Computer Win!"
            -1 -> textView.text = "You Win!"
        }

        if(score == 1 || score == -1){

            scoreUpdate(score)
            buttonsClickable(false)

        } else {

            if(!isThereAnyEmptySpace()){
                buttonsClickable(false)
                textView.text = "TIE!"
            }

        }



    }

    fun isThereAnyEmptySpace() : Boolean {


        for (box in currentPosition.positionArray){

            if(box == elements.EMPTY){

                return true

            }
        }

        return false


    }


    fun finishGame(){
        currentPosition = Position(elements)
        fillGame(currentPosition.positionArray)

        buttonsClickable(true)

    }

    fun buttonsClickable(boolean: Boolean){

        button1.isClickable = boolean
        button2.isClickable = boolean
        button3.isClickable = boolean
        button4.isClickable = boolean
        button5.isClickable = boolean
        button6.isClickable = boolean
        button7.isClickable = boolean
        button8.isClickable = boolean
        button9.isClickable = boolean

    }

    fun makeMove( whoseTour : Char){

        calculateAllPositions(currentPosition, whoseTour )
        val newPosition = getMaxScoreMove(currentPosition)
        createnewPosition(newPosition)
    }

    fun calculateAllPositions(currentPosition : Position, whoseTour : Char){

        var leafMi = true

        for (box_index in 0 until 9){

            //if box is empty
            if(currentPosition.positionArray[box_index] == elements.EMPTY){ //yan yana kardeşlerinde dolaşır bunlar arasında user en küçük skoru olanı, comp. en yüksek skoru olanı alır.

                leafMi = false

                val copyCurrentPosition = currentPosition.positionArray.copyOf()

                copyCurrentPosition[box_index] = whoseTour

                val newPosition = Position(elements)
                newPosition.positionArray = copyCurrentPosition

                newPosition.parentOlustur(currentPosition)

                newPosition.whoesTour = whoseTour

                currentPosition.childArray.add(newPosition)

                // newPosition => child, currentPosition => parent

                giveScoreThePosition(newPosition)
                if(newPosition.score == 1 || newPosition.score == -1){

                    leafMi = true
                    break

                }else{

                    //childına geçer
                    if(whoseTour == COMPUTER){
                        calculateAllPositions(newPosition, USER)
                    }
                    if(whoseTour == USER){
                        calculateAllPositions(newPosition, COMPUTER)
                    }
                }

            }

        }

        if(leafMi){

            if(whoseTour == USER){
                currentPosition.whoesTour = COMPUTER
            } else {
                currentPosition.whoesTour = USER
            }
            giveScoreThePosition(currentPosition)
            giveScores(currentPosition)

        }

    }

    fun giveScores(position: Position){ //bu fonk parentları ile puanlarını takaslayan fonk. leaf düğümlerle işlem yapılır

        //parentın turu comp ise childlar arasından min al, parentın turu user ise childlar arasından max al

        //leaf düğümlerinin parentları ile skorlarına bakılır,
        // eğer leaf düğümlerinin turu user ise parenta min verirler, (böylece comp. min değerler arasından max değeri seçebilir)
        // eğer leaf düğümlerinin turu comp ise parenta max verirler, (böylece user max değerler arasında min değeri seçecektir)
        //position ile ilk gelen değerin leaf düğümleri olduğunu varsayıyoruz

        position.whoesTour?.let { whoesTour ->
            if(whoesTour == USER){
                //userın childları arasında max değerdeki skoru alması lazım
                if(position.childArray.size != 0){
                    getMaxScore(position)
                }
            }

            if(whoesTour == COMPUTER){
                //computerın childlar arasından min skora sahip olanları alması lazım
                if(position.childArray.size != 0){
                    getMinScore(position)
                }
            }

            if(position.parent != null){
                giveScores(position.parent!!)
            }
        }

    }

    fun getMaxScoreMove(position : Position) : Position{

        var max = -100
        var move = Position(elements)
        for(position in position.childArray){
            if(position.score > max){
                max = position.score
                move = position
            }
        }

        return move

    }

    fun getMinScore(parent : Position){

        var min = 100

        for(position in parent.childArray){

            if(position.score < min){
                min = position.score
            }

        }

        parent.score = min
    }
    fun getMaxScore(parent : Position){

        var max = -100

        for(position in parent.childArray){

            if(position.score > max){
                max = position.score
            }

        }

        parent.score = max

    }

    fun giveScoreThePosition(position : Position){

        //yatay başarı
        for(i in 0 until 9 step 3){

            if( position.positionArray[i] == position.positionArray[i+1] &&
                position.positionArray[i] == position.positionArray[i+2]){

                if(position.positionArray[i] == COMPUTER){
                    position.score = 1
                }else if(position.positionArray[i] == USER){
                    position.score = -1
                }


            }

        }

        //dikey başarı
        for(i in 0 until 3){

            if( position.positionArray[i] == position.positionArray[i+3] &&
                position.positionArray[i] == position.positionArray[i+6]){

                if(position.positionArray[i] == COMPUTER){
                    position.score = 1
                }else if(position.positionArray[i] == USER){
                    position.score = -1
                }

            }

        }

        //çapraz başarı
        if( position.positionArray[0] == position.positionArray[4] &&
            position.positionArray[0] == position.positionArray[8]){

            if(position.positionArray[0] == COMPUTER){
                position.score = 1
            }else if(position.positionArray[0] == USER){
                position.score = -1
            }

        }

        if( position.positionArray[2] == position.positionArray[4] &&
            position.positionArray[2] == position.positionArray[6]){

            if(position.positionArray[2] == COMPUTER){
                position.score = 1
            }else if(position.positionArray[2] == USER){
                position.score = -1
            }

        }


    }

    fun positionTest(position : String){

        currentPosition.positionArray = position.toCharArray()
        fillGame(currentPosition.positionArray)
        makeMove(COMPUTER)
    }

}