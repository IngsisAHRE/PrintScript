package formatter

import ASTNode
import AssignmentExpression

class AssignmentExpressionFormatter : FormatterInterface {
    override fun format(
        astNode: ASTNode,
        configMap: Map<String, Any?>,
    ): String {
        astNode as AssignmentExpression
        val spacesInAssignSymbol = configMap["spacesInAssignSymbol"]?.let { it as Int } ?: 0
        return astNode.left.name + spaces(spacesInAssignSymbol) + '=' + spaces(spacesInAssignSymbol) +
            ExpressionFormatter().format(astNode.right, configMap)
    }

    private fun spaces(n: Int): String = " ".repeat(n)
}