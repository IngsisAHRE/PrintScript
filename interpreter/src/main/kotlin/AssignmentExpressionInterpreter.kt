class AssignmentExpressionInterpreter(
    private val variableMap: Map<String, VariableInfo>,
    private val version: String,
) : Interpreter {
    override fun interpret(node: ASTNode): Map<String, VariableInfo> {
        require(node is AssignmentExpression) { "Node must be an AssignmentExpression" }
        val id = node.left.name
        val variable = variableMap[id] ?: throw IllegalArgumentException("Variable not found")
        require(variable.isMutable == true) { "Variable is not mutable: $id" }
        val newValue =
            when (val right = node.right) {
                is Literal -> right.value
                is BinaryExpression -> BinaryExpressionInterpreter(variableMap, version).interpret(right)
                is Identifier -> IdentifierInterpreter(variableMap, version).interpret(right)
                is CallExpression -> CallExpressionInterpreter(variableMap, version).interpret(right)
                else -> throw IllegalArgumentException("Node not found")
            }
        checkTypeMatches(variable, newValue)
        val updatedMap = variableMap.toMutableMap()
        updatedMap[id] = variable.copy(value = newValue.toString())
        return updatedMap
    }

    private fun checkTypeMatches(
        variable: VariableInfo,
        newValue: Any,
    ) {
        val expectedType =
            when {
                newValue is Number -> "number"
                newValue is String -> "string"
                newValue is Boolean && VersionChecker().versionIsSameOrOlderThanCurrentVersion("1.1.0", version) -> "bool"
                else -> throw IllegalArgumentException("Unsupported value type: ${newValue::class.simpleName}")
            }
        require(variable.type == expectedType) {
            "Type mismatch: expected ${variable.type}, got $expectedType"
        }
    }
}
