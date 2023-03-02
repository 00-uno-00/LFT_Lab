# Insiemi guida

- $GUIDA( prog \rightarrow SlEOF) = First(Sl)  = First(S)  = \{Assign, print, read, while, conditional, \{\space\}$

- $GUIDA(Sl\rightarrow SSlp) = First(S)  = \{Assign, print, read, while, conditional, \{\space\}$

- $GUIDA(Slp \rightarrow;SSlp) = \{;\}$

- $GUIDA(Slp \rightarrow \varepsilon) = Follow(Slp)= Follow(Sl)= \{EOF, \}\space\}$

- $GUIDA(S \rightarrow assign<E>to<Il>) = \{assign\}$

- $GUIDA(S \rightarrow print[<El>]) = \{print\}$

- $GUIDA(S \rightarrow read[<Il>]) = \{read\}$

- $GUIDA(S \rightarrow while(<Be>)<S>) = \{while\}$

- $GUIDA(S \rightarrow conditional[<Ol>]S') = \{conditional\}$

- $GUIDA(S' \rightarrow end) = \{end\}$

- $GUIDA(S' \rightarrow else <S> end) = \{else\}$

- $GUIDA(S \rightarrow \{<Sl>\}) = \{\ \{ \space\}$
    
- $GUIDA(Il \rightarrow IDIlp) = \{ID\}$

- $GUIDA(Ilp \rightarrow ,IDIlp) = \{ ,\}$

- $GUIDA(Ilp \rightarrow \varepsilon) = Follow(Idlp) = Follow(Idl) = \{\space],\space ;\space, EOF\}$

- $GUIDA(Ol \rightarrow OiOlp) = First(Oi) = \{Option\}$

- $GUIDA(Olp \rightarrow OiOlp) = First(Oi) = \{Option\}$

- $GUIDA(Ol \rightarrow \varepsilon) = Follow(Ol) = \{\space]\}$

- $GUIDA(Oi \rightarrow Option(<BE>)do<S>) = \{Option\}$

- $GUIDA(BE \rightarrow RELOP<expr><expr>) = \{RELOP\}$

- $GUIDA( E \rightarrow +(<El>)) = \{+ \}$

- $GUIDA( E \rightarrow -<E><E>) = \{- \}$

- $GUIDA( E \rightarrow *(<El>)) = \{* \}$

- $GUIDA( E \rightarrow /<E><E>) = \{/ \}$

- $GUIDA( E \rightarrow NUM) = \{NUM \}$

- $GUIDA( E \rightarrow ID) = \{ID \}$

- $GUIDA( El \rightarrow <E><El_p>) = First(E) = \{+,-,*,/,NUM,ID \}$

- $GUIDA( El_p \rightarrow ,<E><El_p>) = \{, \}$

- $GUIDA( El_p \rightarrow \varepsilon) = Follow(El_p) = Follow(El) = \{),] \}$
