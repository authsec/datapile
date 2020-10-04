Checklists in LaTeX
###################

:date: 2015-09-24T23:40:27+02:00
:tags: LaTeX
:category: Infrastructure
:author: Jens Frey
:summary: How to create a checkbox in LaTeX?

Did you ever feel the need for a simple checkbox in a letter? So did I. After searching for a while I did not really come up with a simple copy and pasteable solution to this problem. Most solutions seem to prefer the :code:`hyperref` package which enables you to put interactive, clickable checkboxes inside your PDF document, but what if you actually need to physically send the document?

As it turns out the solution is actually not that hard, a few :code:`mbox`\es and :code:`adjustbox`\es later I had a solution that did the trick for me.

At the very basic of the box is this block which actually creates the box *and* centers the text behind it in the middle of the box.

.. code-block:: latex
   
   \mbox{\adjustbox{stack=cc,fbox}{\makebox(6,6){}} Yes I will.}\\


The parameters of the :code:`makebox` command define how big the box to check will actually be.

If you want to add a place where the future checkbox checker can put his signature to sign that he checked one of the checkboxes, you can additionally put something like this into your page, which will render the supplied text and a slightly subscript line.

.. code-block:: latex
   
   \mbox{\begin{minipage}[t]{18ex}
        Date and signature
      \end{minipage}
      \hspace{1ex}\rule[-3ex]{12cm}{.5pt}}

Where the value :code:`18ex` of the :code:`minipage` environment controls the width of the box before the line. The parameters of the :code:`rule` command are the position of the line (:code:`-3ex`) (which makes it subscript, if you want that superscript make this value positive), the length of the rule (:code:`12cm`) and the thickness of it (:code:`.5pt`)

So if you pimp this a little bit the code for a full letter with checkboxes looks like `this <{static}/downloads/checkbox-letter.tex>`_.
