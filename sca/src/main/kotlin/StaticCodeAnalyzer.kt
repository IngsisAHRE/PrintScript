import analyzers.CallExpressionAnalyzer
import analyzers.ExpressionStatementAnalyzer
import analyzers.IdentifierAnalyzer
import analyzers.VariableDeclarationAnalyzer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class StaticCodeAnalyzer {
    fun analyze(
        astNode: ASTNode,
        lineIndex: Int,
        scaConfigJsonPath: String,
        version: String,
    ): String {
        val mapper = jacksonObjectMapper()
        val configMap: Map<String, Any?> = mapper.readValue(File(scaConfigJsonPath).readText())

        return when (astNode) {
            is VariableDeclaration -> VariableDeclarationAnalyzer().analyze(astNode, configMap, lineIndex, version)
            is ExpressionStatement -> ExpressionStatementAnalyzer().analyze(astNode, configMap, lineIndex, version)
            is CallExpression -> CallExpressionAnalyzer().analyze(astNode, configMap, lineIndex, version)
            is Identifier -> IdentifierAnalyzer().analyze(astNode, configMap, lineIndex, version)
            else -> ""
        }
    }
}
