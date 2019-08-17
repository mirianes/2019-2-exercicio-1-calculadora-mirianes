package br.ufpe.cin.android.calculadora

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_7.setOnClickListener {
            val exp = text_calc.text
            exp.append("7")
            text_calc.text = exp
        }

        btn_8.setOnClickListener {
            val exp = text_calc.text
            exp.append("8")
            text_calc.text = exp
        }

        btn_9.setOnClickListener {
            val exp = text_calc.text
            exp.append("9")
            text_calc.text = exp
        }

        btn_Divide.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp/")
        }

        btn_4.setOnClickListener {
            val exp = text_calc.text
            exp.append("4")
            text_calc.text = exp
        }

        btn_5.setOnClickListener {
            val exp = text_calc.text
            exp.append("5")
            text_calc.text = exp
        }

        btn_6.setOnClickListener {
            val exp = text_calc.text
            exp.append("6")
            text_calc.text = exp
        }

        btn_Multiply.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp*")
        }

        btn_1.setOnClickListener {
            val exp = text_calc.text
            exp.append("1")
            text_calc.text = exp
        }

        btn_2.setOnClickListener {
            val exp = text_calc.text
            exp.append("2")
            text_calc.text = exp
        }

        btn_3.setOnClickListener {
            val exp = text_calc.text
            exp.append("3")
            text_calc.text = exp
        }

        btn_Subtract.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp-")
        }

        btn_Dot.setOnClickListener {
            val exp = text_calc.text
            exp.append(".")
            text_calc.text = exp
        }

        btn_0.setOnClickListener {
            val exp = text_calc.text
            exp.append("0")
            text_calc.text = exp
        }

        btn_Add.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp+")
        }

        btn_LParen.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp(")
        }

        btn_RParen.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp)")
        }

        btn_Power.setOnClickListener {
            val exp = text_calc.text.toString()
            text_calc.setText("$exp^")
        }

        btn_Clear.setOnClickListener {
            text_calc.setText("")
        }

        btn_Equal.setOnClickListener {
            val value = eval(text_calc.text.toString())
            text_info.text = "$value"
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
