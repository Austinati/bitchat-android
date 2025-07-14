package com.bitchat.android.parsing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log

/**
 * Composable components for rendering parsed message elements
 */

/**
 * Render a list of message elements with proper inline layout
 */
@Composable
fun ParsedMessageContent(
    elements: List<MessageElement>,
    modifier: Modifier = Modifier
) {
    // Use a Column with proper text and special element rendering
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var currentTextRow = mutableListOf<MessageElement>()
        
        for (element in elements) {
            when (element) {
                is MessageElement.Text -> {
                    // Add text to current row
                    currentTextRow.add(element)
                }
                is MessageElement.CashuPayment -> {
                    // Flush any accumulated text first
                    if (currentTextRow.isNotEmpty()) {
                        TextRow(elements = currentTextRow.toList())
                        currentTextRow.clear()
                    }
                    
                    // Show the payment chip on its own row
                    CashuPaymentChip(
                        token = element.token,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
        
        // Flush any remaining text
        if (currentTextRow.isNotEmpty()) {
            TextRow(elements = currentTextRow.toList())
        }
    }
}

/**
 * Render a row of text elements
 */
@Composable
fun TextRow(elements: List<MessageElement>) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        elements.forEach { element ->
            when (element) {
                is MessageElement.Text -> {
                    Text(
                        text = element.content,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Monospace
                    )
                }
                else -> { /* Skip non-text elements */ }
            }
        }
    }
}

/**
 * Chip component for displaying Cashu payments inline
 */
@Composable
fun CashuPaymentChip(
    token: ParsedCashuToken,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { handleCashuPayment(token) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E8B57) // Sea green
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Payment icon
            Text(
                text = "⚡",
                fontSize = 16.sp,
                color = Color.White
            )
            
            // Payment text
            Text(
                text = "Cashu Payment: ${token.amount} ${token.unit}",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace
            )
            
            // Receive button
            Button(
                onClick = { handleCashuPayment(token) },
                modifier = Modifier.height(28.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF2E8B57)
                )
            ) {
                Text(
                    text = "Receive",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Handle Cashu payment interaction
 */
private fun handleCashuPayment(token: ParsedCashuToken) {
    Log.d("CashuPayment", "User clicked Cashu payment: ${token.originalString}")
    Log.d("CashuPayment", "Amount: ${token.amount} ${token.unit}")
    Log.d("CashuPayment", "Mint: ${token.mintUrl}")
    if (token.memo != null) {
        Log.d("CashuPayment", "Memo: ${token.memo}")
    }
    Log.d("CashuPayment", "Proofs: ${token.proofCount}")
    
    // TODO: Implement wallet integration
}
