package inputter

import interpreter.CallExpressionInterpreter
import interpreter.IdentifierInterpreter
import interpreter.VariableInfo
import utils.ASTNode
import utils.CallExpression
import utils.Identifier
import utils.InputProvider
import utils.OutputProvider
import utils.StringLiteral

class ConsoleInputter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
    private val outputProvider: OutputProvider,
    private val inputProvider: InputProvider,
) : Inputter {
    override fun readInput(node: ASTNode): Any {
        val text =
            when (node) {
                is StringLiteral -> node.value
                is Identifier -> {
                    val result =
                        IdentifierInterpreter(variableMap, version).interpret(node)
                            ?: throw IllegalArgumentException("Variable ${node.name} is not initialized at (${node.line}:${node.start})")
                    if (result is String) {
                        result
                    } else {
                        throw IllegalArgumentException(
                            "Expected String argument but was ${result::class.java.simpleName} " +
                                "at (${node.line}:${node.start})",
                        )
                    }
                }
                is CallExpression -> {
                    val result = CallExpressionInterpreter(variableMap, version, outputProvider, inputProvider).interpret(node)
                    if (result is String) {
                        result
                    } else {
                        throw IllegalArgumentException(
                            "Expected String argument but was ${result::class.java.simpleName} " +
                                "at (${node.line}:${node.start})",
                        )
                    }
                }
                else -> ""
            }
        print(text)
        val input = inputProvider.readInput(text)
        return when {
            input?.matches("true|false".toRegex()) == true -> input.toBoolean()
            input?.matches("\\d+(\\.\\d+)?".toRegex()) == true -> {
                input.toIntOrNull() ?: (input.toDoubleOrNull() ?: "")
            }
            input?.matches("\"[^\"]*\"|'[^']*'".toRegex()) == true -> input.removeSurrounding("\"").removeSurrounding("'")
            else -> input
        }
    }
}