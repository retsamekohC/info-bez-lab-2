import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFRun
import java.io.File
import java.io.FileOutputStream


fun main(args: Array<String>) {
    val oldDocument = XWPFDocument(File(args[1]).inputStream())

    val txtFileContent = File(args[0]).readText()
    val maxIndex = txtFileContent.length
    var currentIndex = 0

    val FONT_SIZE_MODIFIER = 30

    val newDocument = XWPFDocument()

    for (i in 0..<oldDocument.paragraphs.size) {
        val curP = oldDocument.paragraphs[i]
        val newParagraph = newDocument.createParagraph()

        for (j in 0..<curP.runs.size) {
            val run = curP.runs[j]

            if (currentIndex != maxIndex) {
                val runText = run.text()

                val currenTxtFileChar = txtFileContent[currentIndex]
                val indexInRun = runText.indexOf(currenTxtFileChar)
                val charInRun = if (indexInRun == -1) {
                    currenTxtFileChar
                } else {
                    runText[indexInRun]
                }
                val split = runText.split(charInRun, limit = 2)
                if (split.size == 2) {
                    val newRunLeft = run.cloneRunShape(newParagraph.createRun())
                    newRunLeft.setText(split[0])

                    val steganographedRun = run.cloneRunShape(newParagraph.createRun())
                    steganographedRun.setText("$charInRun")
                    steganographedRun.fontSize = newRunLeft.fontSize + FONT_SIZE_MODIFIER

                    val newRunRight = run.cloneRunShape(newParagraph.createRun())
                    newRunRight.setText(split[1])
                    currentIndex++
                } else {
                    val cloned = run.cloneRunShape(newParagraph.createRun())
                    cloned.setText(run.text())
                }
            } else {
                val cloned = run.cloneRunShape(newParagraph.createRun())
                cloned.setText(run.text())
            }
        }
        newParagraph.style = curP.style
    }

    if (currentIndex != maxIndex) {
        println("Не вышло поместить текст в вордовский файл")
    } else {
        val wordFile = File("output_word_doc.docx")
        val fileOut = FileOutputStream(wordFile)
        newDocument.write(fileOut)
        newDocument.close()
        fileOut.flush()
        fileOut.close()
    }
}

fun XWPFRun.cloneRunShape(newParRun: XWPFRun):XWPFRun {
    val oldCTRPr = this.ctr.rPr
    if (oldCTRPr != null) {
        if (oldCTRPr.rStyle !== null) {
            val carStyle = this.getStyle()
            newParRun.setStyle(carStyle)
        }
    }
    return newParRun
}