package com.onimaskesi.tic_tac_toe_minmax

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {

    var currentPosition = Position()
    val elements = Elements()
    val USER = elements.USER
    val COMPUTER = elements.COMPUTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //positionTest("OOXX O  X")
        //positionTest("O   O   X")

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

            makeMove(COMPUTER)

        } else {

            button.isClickable = false

        }
    }

    fun playAgainClick(view : View){
        textView.text = ""
        finishGame()
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
        newPosition.parent = Position()
        newPosition.score = 0
        newPosition.whoesTour = null
        currentPosition = newPosition

        /*
        if(isGameFin() != 7){
            finishGame()
        }
         */

        when (isGameFin()) {
            1 -> textView.text = "Computer Win!"
            2 -> textView.text = "You Win!"
            0 -> textView.text = "TIE!"
        }
    }

    fun isGameFin(): Int { // 1 ise Comp kazanır, -1 ise User kazanır, 0 beraberlik, 7 devam

        // oyun bitti mi
        //kazanan var mı
        giveScoreThePosition(currentPosition)
        if(currentPosition.score == 1 || currentPosition.score == -1){

            return currentPosition.score

        }

        //boş yer kaldı mı
        var bosYerKalmamis = true
        for (box in currentPosition.positionArray){
            if(box == elements.EMPTY){
                bosYerKalmamis = false
            }
        }
        if(bosYerKalmamis){

            return 0

        }

        return 7

    }

    fun finishGame(){
        currentPosition = Position()
        fillGame(currentPosition.positionArray)

        button1.isClickable = true
        button2.isClickable = true
        button3.isClickable = true
        button4.isClickable = true
        button5.isClickable = true
        button6.isClickable = true
        button7.isClickable = true
        button8.isClickable = true
        button9.isClickable = true

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

                val newPosition = Position()
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
        var move = Position()
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