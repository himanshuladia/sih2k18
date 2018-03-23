# For extracting specific pages from pdf

import PyPDF2

PDFfilename = '1.pdf'

pfr = PyPDF2.PdfFileReader(open(PDFfilename, 'rb'))

pg2 = pfr.getPage(1)

pfw = PyPDF2.PdfFileWriter()

pfw.addPage(pg2)

NewPDFfilename = '1_new.pdf'

with open(NewPDFfilename, 'wb') as f:
    pfw.write(f)