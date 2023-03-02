# Insiemi guida

## Null

- $null( Ep )$
- $null( Tp )$
  
## First

- $GUIDA( start \rightarrow EEOF) = First(E)  = First(T)  = \{(, num \}$

- $GUIDA( E \rightarrow TE_p) = First(T) = \{(, num \}$

- $GUIDA( E_p \rightarrow +TE_p) = First(+) = \{+\}$

- $GUIDA( E_p \rightarrow -TE_p) = First(-) = \{-\}$

- $GUIDA( E_p \rightarrow \varepsilon) = Follow(E_p) = Follow(E) = \{), EOF\}$

- $GUIDA( T \rightarrow FT_p) = First(F) = \{(, num \}$

- $GUIDA( T_p \rightarrow *FT_p) = First(*) = \{*\}$

- $GUIDA( T_p \rightarrow /FT_p) = First(/) = \{/\}$

- $GUIDA( T_p \rightarrow \varepsilon) = Follow(T_p) = Follow(T) = \{+, -, ), EOF\}$

- $GUIDA(F \rightarrow num ) = \{num\}$

- $GUIDA( F \rightarrow (E)) = First(() = \{(\}$

- $First(T) = First(F) = \{(,num\}$

- $First(F) = \{(,num\}$

