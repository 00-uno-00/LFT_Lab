.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
 iload 0
 ldc 0
 if_icmpge L2
 iload 0
 invokestatic Output/print(I)V
 goto L1
L2:
 iload 0
 ldc 1
 if_icmpne L3
L4:
 iload 0
 ldc 5
 if_icmpge L5
 iload 0
 invokestatic Output/print(I)V
 ldc 1
 iload 0
 iadd 
 istore 0
 goto L4
L5:
 goto L1
L3:
 iload 0
 ldc 2
 if_icmpne L6
 iload 0
 iload 0
 imul 
 invokestatic Output/print(I)V
 goto L1
L6:
 iload 0
 ldc 100
 iadd 
 invokestatic Output/print(I)V
L1:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

