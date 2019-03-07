//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.model.core.*
import lzero.domain.scanning.ELZeroTokenType
import lzero.domain.scanning.LZeroToken

//---------------------------------------------------------------------------------------------------------------------

class LZeroParser(
    private val fileName: String,
    code: String
) {

    private val input = LZeroExpectedTokenBufferImpl(code)

    ////

    /**
     * element
     *   : documentation? annotations concept qualifiedName? uuid? ( "(" parameters? ")" )? connections
     *   ;
     */
    fun parseElement(): LZeroElement {

        // expecting(ELZeroTokenType.DOCUMENTATION_LINE, ELZeroTokenType.IDENTIFIER, ELZeroTokenType.CONCEPT_KEYWORD)

        // documentation?
        val documentation = parseDocumentationOpt()

        // expecting(ELZeroTokenType.IDENTIFIER, ELZeroTokenType.CONCEPT_KEYWORD)

        // annotations
        val annotations = parseAnnotations()

        // expecting(ELZeroTokenType.CONCEPT_KEYWORD)

        // concept
        val concept = parseConcept()

        // expecting(ELZeroTokenType.IDENTIFIER, ELZeroTokenType.UUID, ELZeroTokenType.LEFT_PARENTHESIS,
        //           ELZeroTokenType.COLON, ELZeroTokenType.CONNECTOR_KEYWORD, ELZeroTokenType.SEMICOLON)

        // qualifiedName?
        val qualifiedName = parseQualifiedNameOpt()

        // expecting(ELZeroTokenType.UUID, ELZeroTokenType.LEFT_PARENTHESIS,
        //           ELZeroTokenType.COLON, ELZeroTokenType.CONNECTOR_KEYWORD, ELZeroTokenType.SEMICOLON)

        // uuid?
        val uuid = parseUuidOpt()

        // TODO: arguments

        // TODO: parameters

        // connections
        val connections = parseConnections()

        // Put together the element from its pieces.
        return LZeroElement(documentation, annotations, concept, qualifiedName, uuid, connections)
    }

    ////

    /**
     * Parses one annotation.
     *
     * annotation
     *   : identifier ( "(" arguments ")" )?
     *   ;
     */
    private fun parseAnnotation(): LZeroAnnotation {

        val annotationName = input.read(ELZeroTokenType.IDENTIFIER)

        // TODO: arguments

        return LZeroAnnotation(annotationName.origin, annotationName.text)

    }

    /**
     * Parses a possibly empty list of annotations.
     *
     * annotations
     *   : annotation*
     *   ;
     */
    private fun parseAnnotations(): LZeroAnnotationList {

        val annotations = mutableListOf<LZeroAnnotation>()

        while (input.hasLookAhead(ELZeroTokenType.IDENTIFIER)) {
            annotations.add(parseAnnotation())
        }

        return LZeroAnnotationList(annotations)
    }

    /**
     * Parses the next token, which is expected to be a concept keyword.
     *
     * concept
     *   : CONCEPT_KEYWORD
     *   ;
     */
    private fun parseConcept(): LZeroConcept {

        val conceptToken = input.read(ELZeroTokenType.CONCEPT_KEYWORD)

        return LZeroConcept(conceptToken.origin, conceptToken.text)

    }

    /**
     * connected
     *   : qualifiedName
     *   : uuid
     *   : "[" ( element ","? )* "]"
     *   : "(" expression  ")"
     *   ;
     */
    private fun parseConnected() : LZeroConnected {

        if ( input.hasLookAhead(ELZeroTokenType.IDENTIFIER)) {
            return LZeroConnectedQualifiedName(parseQualifiedName())
        }

        if ( input.hasLookAhead(ELZeroTokenType.UUID)) {
            return LZeroConnectedUuid(parseUuid())
        }

        throw UnsupportedOperationException( "TODO" )

//        if ( input.hasLookAhead(ELZeroTokenType.LEFT_BRACKET)) {
//            return parseConnectedList()
//        }
//
//        return parseConnectedExpression()

    }

    /**
     * Parses a list of connections (that can be empty).
     *
     * connections
     *   : implicitConnection+ explicitConnection* (containment | valueAssignment)?
     *   : explicitConnection+ (containment | valueAssignment)?
     *   : containment
     *   : valueAssignment
     *   : ";"
     *   ;
     */
    private fun parseConnections(): LZeroConnectionList {

        val connections = mutableListOf<LZeroConnection>()

        while (input.hasLookAhead(ELZeroTokenType.COLON)) {
            connections.add(parseImplicitConnection())
        }

        while (input.hasLookAhead(ELZeroTokenType.CONNECTOR_KEYWORD)) {
            connections.add(parseExplicitConnection())
        }

        if (input.hasLookAhead(ELZeroTokenType.LEFT_BRACE)) {
            connections.add(parseContainment())
        }
        else if (input.hasLookAhead(ELZeroTokenType.EQ)) {
            connections.add(parseValueAssignment())
        }

        if (connections.isEmpty()) {
            input.read(ELZeroTokenType.SEMICOLON)
        }

        return LZeroConnectionList(connections)

    }

    /**
     * Parses a list of contained concepts.
     *
     * containment
     *   : "{" element "}"
     *   ;
     */
    private fun parseContainment() : LZeroContainment {

        val leftBrace = input.read(ELZeroTokenType.LEFT_BRACE)

        // TODO: "=" expression

        val containedElements = mutableListOf<LZeroElement>()

        while (!input.hasLookAhead(ELZeroTokenType.RIGHT_BRACE)) {
            containedElements.add(parseElement())
        }

        return LZeroContainment(leftBrace.origin, containedElements)

    }

    /**
     * Parses an optional block of documentation.
     */
    private fun parseDocumentationOpt(): LZeroDocumentation? {

        if ( input.hasLookAhead(ELZeroTokenType.DOCUMENTATION) ) {
            return parseDocumentation()
        }

        return null

    }

    /**
     * Parses zero or one block of documentation.
     *
     * documentation
     *   : DOCUMENTATION?
     *   ;
     */
    private fun parseDocumentation(): LZeroDocumentation {

        val token = input.read(ELZeroTokenType.DOCUMENTATION)
        return LZeroDocumentation(token.origin, token.text)

    }

    /**
     * Parses an explicit connection (one where the connector is spelled out).
     *
     * explicitConnection
     *   : CONNECTOR_KEYWORD ( "(" parameters? ")" )? "~" connected
     *   ;
     */
    private fun parseExplicitConnection(): LZeroExplicitConnection {

        val keyword = input.read(ELZeroTokenType.CONNECTOR_KEYWORD)

        val connector = LZeroConnector(keyword.origin, keyword.text)

        // TODO: parameters

        input.read(ELZeroTokenType.TILDE)

        return LZeroExplicitConnection(connector, parseConnected())

    }

    /**
     * Parses an implicit connection (just a colon with connection type implied by the concept and connected).
     *
     * implicitConnection
     *   : ":" connected
     *   ;
     */
    private fun parseImplicitConnection(): LZeroImplicitConnection {

        val colonToken = input.read(ELZeroTokenType.COLON)

        return LZeroImplicitConnection(colonToken.origin, parseConnected())

    }

    /**
     * Parses an optional qualified name.
     */
    private fun parseQualifiedNameOpt(): LZeroQualifiedName? {

        if (!input.hasLookAhead(ELZeroTokenType.IDENTIFIER)) {
            return null
        }

        return parseQualifiedName()
    }

    /**
     * Parses an optional qualified name.
     *
     * qualifiedName
     *   : name ( "." name )*
     *   ;
     */
    private fun parseQualifiedName(): LZeroQualifiedName {

        val names = mutableListOf<LZeroName>()

        while (true) {

            val token = input.read(ELZeroTokenType.IDENTIFIER)
            names.add(LZeroName(token.origin, token.text))

            if (!input.hasLookAhead(2, ELZeroTokenType.IDENTIFIER) && input.consumeWhen(ELZeroTokenType.DOT)) {
                break
            }

        }

        return LZeroQualifiedName(names)
    }

    /**
     * Parses an optional UUID.
     *
     * uuid
     *   : UUID
     *   ;
     */
    private fun parseUuid(): LZeroUuid {

        val uuidToken = input.read(ELZeroTokenType.UUID)

        return LZeroUuid(uuidToken.origin, uuidToken.text)

    }

    /**
     * Parses an optional UUID.
     *
     * uuid
     *   : UUID
     *   ;
     */
    private fun parseUuidOpt(): LZeroUuid? {

        if ( !input.hasLookAhead(ELZeroTokenType.UUID) ) {
            return null
        }

        return parseUuid()

    }

    /**
     * Parses a value assignment.
     *
     * valueAssignment
     *   : "=" expression
     *   ;
     */
    private fun parseValueAssignment() : LZeroValueAssignment {

        val equals = input.read(ELZeroTokenType.EQ)

        // TODO: parse the expression (or just literal?)

        return LZeroValueAssignment(equals.origin /*, expression*/)

    }

    ////

    /**
     * Adds the file name to a token origin.
     */
    private val LZeroToken.origin
        get() = LZeroFileOrigin(fileName, this.line, this.column)

}

//---------------------------------------------------------------------------------------------------------------------

