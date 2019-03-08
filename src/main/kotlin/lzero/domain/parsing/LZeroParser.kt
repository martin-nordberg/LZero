//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.model.annotations.LZeroAnnotation
import lzero.domain.model.annotations.LZeroAnnotationList
import lzero.domain.model.connecteds.LZeroConnected
import lzero.domain.model.connecteds.LZeroConnectedQualifiedName
import lzero.domain.model.connecteds.LZeroConnectedUuid
import lzero.domain.model.connections.*
import lzero.domain.model.core.LZeroFileOrigin
import lzero.domain.model.documentation.LZeroBlockDocumentation
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.documentation.LZeroNullDocumentation
import lzero.domain.model.elements.LZeroConcept
import lzero.domain.model.elements.LZeroElement
import lzero.domain.model.names.LZeroName
import lzero.domain.model.names.LZeroNullName
import lzero.domain.model.names.LZeroQualifiedName
import lzero.domain.model.names.LZeroSimpleName
import lzero.domain.model.uuids.LZeroKnownUuid
import lzero.domain.model.uuids.LZeroNullUuid
import lzero.domain.model.uuids.LZeroUuid
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
     *   : declaration
     *   : expression
     *   ;
     */
    fun parseElement(): LZeroElement {

        // documentation?
        val documentation = parseDocumentationOpt()

        // annotations
        val annotations = parseAnnotations()

        // concept
        val concept = parseConcept()

        // qualifiedName?
        val qualifiedName = parseQualifiedNameOpt()

        // uuid?
        val uuid = parseUuidOpt()

        // TODO: arguments

        // TODO: parameters

        // connections
        val connections = parseConnections()

        // Put together the element from its pieces.
        return LZeroElement(
            documentation,
            annotations,
            concept,
            qualifiedName,
            uuid,
            connections
        )
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

        val atToken = input.read(ELZeroTokenType.AT)

        val qualifiedName = parseQualifiedName()

        // TODO: arguments

        return LZeroAnnotation(atToken.origin, qualifiedName)

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

        while (input.hasLookAhead(ELZeroTokenType.AT)) {
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

        val hashToken = input.read(ELZeroTokenType.HASH)

        val qualifiedName = parseQualifiedName()

        return LZeroConcept(hashToken.origin, qualifiedName)

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

        while (input.hasLookAhead(ELZeroTokenType.TILDE)) {
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
    private fun parseDocumentationOpt(): LZeroDocumentation {

        if ( input.hasLookAhead(ELZeroTokenType.DOCUMENTATION) ) {
            return parseDocumentation()
        }

        return LZeroNullDocumentation

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
        return LZeroBlockDocumentation(token.origin, token.text)

    }

    /**
     * Parses an explicit connection (one where the connector is spelled out).
     *
     * explicitConnection
     *   : CONNECTOR_KEYWORD ( "(" parameters? ")" )? "~" connected
     *   ;
     */
    private fun parseExplicitConnection(): LZeroExplicitConnection {

        val tildeToken = input.read(ELZeroTokenType.TILDE)

        val qualifiedName = parseQualifiedName()

        val connector = LZeroConnector(tildeToken.origin, qualifiedName)

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
    private fun parseQualifiedNameOpt(): LZeroName {

        if (!input.hasLookAhead(ELZeroTokenType.IDENTIFIER)) {
            return LZeroNullName
        }

        return parseQualifiedName()
    }

    /**
     * Parses a qualified name.
     *
     * qualifiedName
     *   : name ( "." name )*
     *   ;
     */
    private fun parseQualifiedName(): LZeroName {

        val names = mutableListOf<LZeroSimpleName>()

        while (true) {

            val token = input.read(ELZeroTokenType.IDENTIFIER)
            names.add(LZeroSimpleName(token.origin, token.text))

            if (!input.hasLookAhead(2, ELZeroTokenType.IDENTIFIER) || !input.consumeWhen(ELZeroTokenType.DOT)) {
                break
            }

        }

        if ( names.size > 1 ) {
            return LZeroQualifiedName(names)
        }

        return names[0]

    }

    /**
     * Parses an optional UUID.
     *
     * uuid
     *   : UUID
     *   ;
     */
    private fun parseUuid(): LZeroKnownUuid {

        val uuidToken = input.read(ELZeroTokenType.UUID)

        return LZeroKnownUuid(uuidToken.origin, uuidToken.text)

    }

    /**
     * Parses an optional UUID.
     *
     * uuid
     *   : UUID
     *   ;
     */
    private fun parseUuidOpt(): LZeroUuid {

        if ( !input.hasLookAhead(ELZeroTokenType.UUID) ) {
            return LZeroNullUuid
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

