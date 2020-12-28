---
title: 位运算
date: 2020-08-12
sidebar: auto
categories:
 - 基础
prev: false
next: false
---

## 一、位运算基础
程序中的数在计算机内存中都是以二进制的形式存在的，位运算就是直接对整数在内存中对应的二进制位进行操作。
十进制转二进制时，采用“除 2 取余，逆序排列”法：

- 用 2 整除十进制数，得到商和余数;
- 再用 2 整除商，得到新的商和余数;
- 重复第 1 和第 2 步，直到商为 0;
- 将先得到的余数作为二进制数的高位，后得到的余数作为二进制数的低位，依次排序;
排序结果就是该十进制数的二进制表示。例如十进制数 101 转换为二进制数的计算过程如下：
```c
101 % 2 = 50 余 1
50 % 2 = 25 余 0
25 % 2 = 12 余 1
12 % 2 = 6 余 0
6 % 2 = 3 余 0
3 % 2 = 1 余 1
1 % 2 = 0 余 1
```

逆序排列即二进制中的从高位到低位排序，得到 7 位二进制数为 1100101，如果要转换为 8 位二进制数，就需要在最高位补 0。即十进制数的 8 位二进制数为 01100101。
## 二、原反补码
数值有正负之分，那么仅有 0 和 1 的二进制如何表示正负呢？

人们设定，二进制中最高位为 0 代表正，为 1 则代表负。例如 0000 1100 对应的十进制为 12，而 1000 1100 对应的十进制为 -12。这种表示被称作**原码**。但新的问题出现了，原本二进制的最高位始终为 0，为了表示正负又多出了 1，在执行运算时就会出错。举个例子，1 + (-2) 的二进制运算如下：
```c
0000 0001 + 1000 0010 
= 1000 0011
= -3 
```
这显然是有问题的，问题就处在这个代表正负的最高位。接着，人们又弄出了**反码**（二进制各位置的 0 与 1 互换，例如 0000 1100 的反码为 1111 0011）。此时，运算就会变成这样：
```c
0000 0001 + 1111 1101
= 1111 1110
# 在转换成十进制前，需要再次反码
= 1000 0001 
= -1
```

这次好像正确了。但它仍然有例外，我们来看一下 1 + (-1)：
```c
0000 0001 + 1111 1110
= 1111 1111
= 1000 0000
= -0
```
零是没有正负之分的，为了解决这个问题，就搞出了补码的概念。**补码**是为了让负数变成能够加的正数，所以 负数的补码= 负数的绝对值取反 + 1，例如 -1 的补码为：
```c
-1 的绝对值 1
= 0000 0001 # 1 的二进制原码
= 1111 1110 # 原码取反
= 1111 1111 # +1 后得到补码
```

反过来，由补码推导原码的过程为 原码 = 补码 - 1，再求反。要注意的是，反码过程中，最高位的值不变，这样才能够保证结果的正负不会出错。例如 1 + (-6) 和 1 + (-9) 的运算过程如下：
```c
# 1 的补码 + -6 的补码
0000 0001 + 1111 1010
= 1111 1011 # 补码运算结果
= 1111 1010 # 对补码减 1，得到反码
= 1000 0101 # 反码取反，得到原码
= -5 # 对应的十进制


# 1 的补码 + -9 的补码
0000 0001 + 1111 0111
= 1111 1000 # 补码运算结果
= 1111 0111 # 对补码减 1，得到反码
= 1000 1000 # 反码取反，得到原码
= -8 # 对应的十进制
```
:::warning
正数的补码与原码相同，不需要额外运算。也可以说，补码的出现就是为了解决负数运算时的符号问题。
:::

## 三、运算符

名称|符号
-|-
按位与	| &
按位或	|  `|`
按位异或|	^
按位取反|	~
左移运算|	<<
右移运算|	>>

### 3.1 位与
按位与运算将参与运算的两数对应的二进制位相与，当对应的二进制位均为 1 时，结果位为 1，否则结果位为 0。按位与运算的运算符为 &，参与运算的数以补码方式出现。举个例子，将数字 5 和数字 8 进行按位与运算，其实是将数字 5 对应的二进制 0000 0101 和数字 8 对应的二进制 0000 1000 进行按位与运算，即：
```c
0000 0101
&
0000 1000
```
根据按位与的规则，将各个位置的数进行比对。运算过程如下：
```c
0000 0101
&
0000 1000
---- ----
0000 0000
```

由于它们对应位置中没有“均为 1 ”的情况，所以得到的结果是 0000 0000。
将结果换算成十进制，得到 0，即 5&8 = 0。
### 3.2 位或
按位或运算将参与运算的两数对应的二进制位相或，只要对应的二进制位中有 1，结果位为 1，否则结果位为 0。按位或运算的运算符为 |。举个例子，将数字 3 和数字 7 进行按位或运算，其实是将数字 3 对应的二进制 0000 0011和数字 7 对应的二进制 0000 0111 进行按位或运算，即：
```c
0000 0011
|
0000 0111
```
根据按位或的规则，将各个位置的数进行比对。运算过程如下：
```c
0000 0011
|
0000 0111
---- ----
0000 0111
```
最终得到的结果为 0000 0111。将结果换算成十进制，得到 7，即 3|7 = 7。

### 3.3 按位异或
按位异或运算将参与运算的两数对应的二进制位相异或，当对应的二进制位值不同时，结果位为 1，否则结果位为 0。按位异或的运算符为 ^，参与运算的数以补码方式出现。举个例子，将数字 12 和数字 7 进行按位异或运算，其实是将数字 12 对应的二进制 0000 1100 和数字 7 对应的二进制 0000 0111 进行按位异或运算，即：
```c
0000 1100
^
0000 0111
```
根据按位异或的规则，将各个位置的数进行比对。运算过程如下：
```c
0000 1100
^
0000 0111
---- ----
0000 1011
```
最终得到的结果为 0000 1011。将结果换算成十进制，得到 11，即 12^7 = 11。
### 3.4 按位取反
按位取反运算将二进制数的每一个位上面的 0 换成 1，1 换成 0。按位取反的运算符为 ~，参与运算的数以补码方式出现。举个例子，对数字 9 进行按位取反运算，其实是将数字 9 对应的二进制 0000 1001 进行按位取反运算，即：
```c
～0000 1001
= 0000 1001 # 补码，正数补码即原码
= 1111 1010 # 取反
= -10
```
最终得到的结果为 -10。再来看一个例子，-20 按位取反的过程如下：
```c
～0001 0100
= 1110 1100 # 补码
= 0001 0011 # 取反
= 19
```

最终得到的结果为 19。我们从示例中找到了规律，按位取反的结果用数学公式表示：

`～x = -(x + 1)`
我们可以将其套用在 9 和 -20 上：

```c
～9 = -(9 + 1) = -10
~(-20) = -((-20) + 1) = 19
```

这个规律也可以作用于数字 0 上，即 ~0 = -(0 + 1) = -1。

### 3.5 左移运算
左移运算将数对应的二进位全部向左移动若干位，高位丢弃，低位补 0。左移运算的运算符为 <<。举个例子，将数字 5 左移 4 位，其实是将数字 5 对应的二进制 0000 0101 中的二进位向左移动 4 位，即：
```c
5 << 4
= 0000 0101 << 4
= 0101 0000 # 高位丢弃，低位补 0
= 80
```
最终结果为 80。这等效于:`x << n = x * (2) ^ n`
### 3.6 右移运算
右移运算将数对应的二进位全部向右移动若干位。对于左边的空位，如果是正数则补 0，负数可能补 0 或 1 （Turbo C 和很多编译器选择补 1）。右移运算的运算符为 >>。举个例子，将数字 80 右移 4 位，其实是将数字 80 对应的二进制 0101 0000 中的二进位向右移动 4 位，即：
```c
80 >> 4
= 0101 0000 >> 4
= 0000 0101 # 正数补0，负数补1 
= 5
```
最终结果为 5。这等效于：`x >> n = x / (2) ^ n`

## 四、位运算的应用
### 4.1 判断数字奇偶
通常，我们会通过取余来判断数字是奇数还是偶数。例如判断 101 的奇偶用的方法是：
```python
# python
if 101 % 2:
    print('偶数')
else:
    print('奇数')
```
也可以通过位运算中的按位与来实现奇偶判断，例如：
```python
# python
if 101 & 1:
    print('奇数')
else:
    print('偶数')
```
这是因为奇数的二进制最低位始终为 1，而偶数的二进制最低为始终为 0。所以，无论任何奇数与 1 即 0000 0001 相与得到的都是 1，任何偶数与其相与得到的都是 0。

### 4.2 变量交换
```python
# python
a, b = 3, 5
a = a ^ b
b = a ^ b
a = a ^ b
print(a, b)
```

### 4.3 求 x 与 2 的 n 次方乘积
设一个数为 x，求 x 与 2 的 n 次方乘积。这用数学来计算都是非常简单的：`x << n = x * (2) ^ n`,在位运算中，要实现这个需求只需要用到左移运算，即 x << n。
