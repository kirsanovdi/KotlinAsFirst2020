package lesson8.task1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.Math.ulp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

class Tests {
    @Test
    @Tag("Example")
    fun pointDistance() {
        assertEquals(0.0, Point(0.0, 0.0).distance(Point(0.0, 0.0)), 1e-5)
        assertEquals(5.0, Point(3.0, 0.0).distance(Point(0.0, 4.0)), 1e-5)
        assertEquals(50.0, Point(0.0, -30.0).distance(Point(-40.0, 0.0)), 1e-5)
    }

    @Test
    @Tag("Example")
    fun halfPerimeter() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).halfPerimeter(), 1e-5)
        assertEquals(2.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).halfPerimeter(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleArea() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).area(), 1e-5)
        assertEquals(0.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).area(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleContains() {
        assertTrue(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(1.5, 1.5)))
        assertFalse(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(2.5, 2.5)))
    }

    @Test
    @Tag("Example")
    fun segmentEquals() {
        val first = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val second = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val third = Segment(Point(3.0, 4.0), Point(1.0, 2.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
    }

    private fun approxEquals(expected: Line, actual: Line, delta: Double): Boolean =
        abs(expected.angle - actual.angle) <= delta && abs(expected.b - actual.b) <= delta

    private fun assertApproxEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun assertApproxNotEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertFalse(approxEquals(expected, actual, delta))
    }

    private fun approxEquals(expected: Point, actual: Point, delta: Double): Boolean =
        expected.distance(actual) <= delta

    private fun assertApproxEquals(expected: Point, actual: Point, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun approxEquals(expected: Segment, actual: Segment, delta: Double): Boolean =
        expected.begin.distance(actual.begin) <= delta && expected.end.distance(actual.end) <= delta ||
                expected.begin.distance(actual.end) <= delta && expected.end.distance(actual.begin) <= delta

    private fun assertApproxEquals(expected: Segment, actual: Segment, delta: Double = ulp(10.0)) {
        //println(expected)
        //println(actual)
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun approxEquals(expected: Circle, actual: Circle, delta: Double): Boolean =
        expected.center.distance(actual.center) <= delta && abs(expected.radius - actual.radius) <= delta

    private fun assertApproxEquals(expected: Circle, actual: Circle, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    @Test
    @Tag("Example")
    fun lineEquals() {
        run {
            val first = Line(Point(0.0, 0.0), 0.0)
            val second = Line(Point(3.0, 0.0), 0.0)
            val third = Line(Point(-5.0, 0.0), 0.0)
            val fourth = Line(Point(3.0, 1.0), 0.0)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 2)
            val second = Line(Point(0.0, 3.0), PI / 2)
            val third = Line(Point(0.0, -5.0), PI / 2)
            val fourth = Line(Point(1.0, 3.0), PI / 2)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 4)
            val second = Line(Point(3.0, 3.0), PI / 4)
            val third = Line(Point(-5.0, -5.0), PI / 4)
            val fourth = Line(Point(3.00001, 3.0), PI / 4)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
    }

    @Test
    @Tag("Example")
    fun triangleEquals() {
        val first = Triangle(Point(0.0, 0.0), Point(3.0, 0.0), Point(0.0, 4.0))
        val second = Triangle(Point(0.0, 0.0), Point(0.0, 4.0), Point(3.0, 0.0))
        val third = Triangle(Point(0.0, 4.0), Point(0.0, 0.0), Point(3.0, 0.0))
        val fourth = Triangle(Point(0.0, 4.0), Point(0.0, 3.0), Point(3.0, 0.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
        assertNotEquals(fourth, first)
    }

    @Test
    @Tag("2")
    fun circleDistance() {
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(1.0, 0.0), 1.0)), 1e-5)
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(0.0, 2.0), 1.0)), 1e-5)
        assertEquals(1.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(-4.0, 0.0), 2.0)), 1e-5)
        assertEquals(2.0 * sqrt(2.0) - 2.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(2.0, 2.0), 1.0)), 1e-5)
    }

    @Test
    @Tag("1")
    fun circleContains() {
        val center = Point(1.0, 2.0)
        assertTrue(Circle(center, 1.0).contains(center))
        assertFalse(Circle(center, 2.0).contains(Point(0.0, 0.0)))
        assertTrue(Circle(Point(0.0, 3.0), 5.01).contains(Point(-4.0, 0.0)))
    }

    @Test
    @Tag("3")
    fun diameter() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        assertApproxEquals(Segment(p5, p6), diameter(p1, p2, p3, p4, p5, p6))
        assertApproxEquals(Segment(p4, p6), diameter(p1, p2, p3, p4, p6))
        assertApproxEquals(Segment(p3, p4), diameter(p1, p2, p3, p4))
        assertApproxEquals(Segment(p2, p4), diameter(p1, p2, p4))
        assertApproxEquals(Segment(p1, p4), diameter(p1, p4))
    }

    //<html><body><p>:[03<s>/mE</s>M/^/dM8nmwUaY#?MO[<b>J</b>PpxBF=TUl'rGuuK%`{BI<b>`Lv</b>'#<s>B</s>jGy<i><b>=</b>bI#w4v66RMI=q8_uX7vpBeOt[</i>,ouz\"_X[OGxK3|6H<b>_IX/9<s>s$su'2:ZW4:V?Iv)!w7,MtQ`</s></b>um$SSG<s>j$'#<b>-Nc{kwm</b>|}|GgR5</s>Kf<s>Z/R;v</s>VEv2BD.<b>DN9/0</b>4&</p><p>jVUl-<s>|#<i>#fi<b></b>Ur%!l</i>K</s>3U{UfX<s>r[<i>f</i>PC#G\"(<b>Mcq</b>%q1!J\"k<i>%+L(</i>+v</s><b>\\1C+<s>Q#/M:X</s>+</b>ba<s><i>fAc\\6!</i>Ww[l<i>%</i>S'POgSr</s>Tf<s>T<i>2bKu6</i>f</s>#u<i>)2</i>t&t&<b>6;H</b>;<s>k</s>8<b>cIL}i-n[<s>!My-Z2Yr%<i>acg0:XdigfPa3</i>F6<i>NG</i>\"1A<i>c</i>Hex</s>I<s>PEd</s>gEJS%Z\"</b>YhkQ^B<i>PTs<b>E<s>A</s>pzi</b></i>E-<b>=</b><b>m7dZ||<s>`&O3</s>Nm_M</b>:<b><i>[</i>46</b>f<b>O<s>4r(Mm<i>D</i>or</s>{'<i><s>u</s><s>Eq</s>q3s<s>jmW)l:`$iHXI</s><s></s>M<s>,E</s>`xlO<s>z^</s>$N</i>{eC\"<s><i>5y{</i>:Vef</s>F<s><i>e=</i>@rj</s>^<s>o\"W<i></i>q<i></i>t<i>dr=</i>rL</s></b>,Smel+PvZwR$A=<s>iqN<i>V=P/</i>9.Z<i>C<b>B</b>B</i>V<b>&</b>d<b>-<i>2</i>O</b>Kp</s>4?<i>k;<b>7R<s>FG</s></b>.\"gy<b>S@</b>J<s>l</s>n</i>7<s></s>XaQ<b>t@[QTn<s>j</s><s>_6z[@<i>!Z1</i>%Tt`Jm4</s><i>E</i>TNI<i>U</i>k5O<i>?bZ:</i>M<s>NF3(ykx<i></i>Q(#`IJ<i></i>,ef.<i>.q</i>q</s>M=<s><i></i>(<i>3_{</i>1a7F2</s>?g</b>z+V^Dslhs]<i>D)2<b>r'cowN$Iuxg}u;D,^q-aXD}q{C,90W\"eoQCPui==I,+2pD4.Gl6mBb4G{AoJq<s>{ZazQARKYyx0Q4`x\"av</s><s>%:s9yQx</s>\"Jz1hl?^}3-0<s>VO$rT1{^:6D^ziQsKn1ji\"^</s>0UxkWE'nT)k2VU|{=5Fdy/q4(6t%RCZ\"Gt,#1r&i7b</b></i>'j4m<s>cJa&<b>|\\0<i>hMc</i>A2\".!?</b>tLM</s>Kh]Z<b>3ml</b>7ny2<i><b><s>mL`JE\"Y</s>u[<s>|R</s>^n</b>cP<s>Lk<b>z6,U</b>Wl<b>VRc`0</b></s>a<b>0.<s>Fj</s>'</b>\"<s>s<b></b>.<b>an</b>_f</s>A<b>E</b>7!/</i>:AN$;0yn<b>qd</b>i<i>pj0<s>4<b>s}9</b>q<b>p</b>.</s><s>dC</s>xN<b>$zW</b>8<s>N]%</s>gI2<s>Nz\\|`L<b>O%;</b>5e<b></b>0w/<b></b>Z=g<b>]r</b>E]</s>j#t?f<s>2</s>k{zT</i>yDmS-<b>&`</b>Qqb<b>l5:<i></i>pr<s>R\"9</s>^e</b>W<i>vd<b>FjKh<s>$Pp</s>8%Q<s>rk</s><s>Lj</s>`<s>Cd</s>V</b>y1{Cl</i>J!E(6RK/FzK{XB0&zL\"[HQ(|(UD(MhrW\\T7L])BfQKZ!/;a]a(J</p><p>{<i>j)HR<b>:)A<s>f,</s>?Y<s>}s@T;v[^s[1</s>,(h4=-(uJbo,l'O<s>R)5^V</s>]{X}</b>SEy:Ui4gS:V;h'B<b>R,30pLY\"ARjQzUs2Kb`&<s>_k+1.WVU</s>F02zKOU)\"F</b>`10dU<b>(<s>FSi</s>t%I]PuSlz</b>+bA''-}6h=&n%dk3<s>\"+8:0%`<b>,z/6D</b>zfj</s>mV8-{</i>3RL<s>;6M<b>#@hy</b>dMZkQqT##{M!</s>+a=,^`<s>)<i>V</i>!/<i>Q%ua1bPrt5</i>=</s><s>1,:VE$</s>p8T`<i>;<s>{<b>b=$@</b>F<b>)</b></s>%<b>L4</b></i>#/g<b>iQ=</b>?^yqC9is<i>1=</i>7L<i>e<b>r9O<s>lDO</s>yg-h=i<s>!6W,Je</s>h</b>u|U{:)g|Pn</i>)p`o<b>$TGwc</b>]<b>?</b>-<b>A<i>1<s></s>9I<s>`</s>}<s></s>1<s>`e</s><s>)</s>u&vm</i>hlj`?w<i>`n</i>[</b>7<i></i>=<b><i>8.WW</i>aJ<s>k</s>J8<s>/</s><i>RYEh</i>+</b>,@]xRpK]0ujJD\"D<i>Pa|R)$2tI:=@`<s>Qd1nrdWZcC={6</s>Q<b>=<s>Ui</s>Q}<s>N</s>7js<s>Bd27/k0JGd/;</s><s>gIDAoh4j6:Q:j:l</s>w<s>mM^zGS</s>qFUY<s>dN76T</s>ry`</b>h,W:k:</i>]<b>$y@)d1<s>C:Y9e1<i></i>Y\\q</s>&HpWm<s>W]4!vE1[</s>7W<s>rBy'P;#X\"</s>S<i>_$lL'<s>lqWDLbY`z/K</s>IIe4<s></s>pn</i>)g)</b>9t<i>OQ{<s></s>sIRi(<b>3[se<s>`$?F-Sv\\P7+@lE!R.!</s></b>LV'w<b>,</b>.g{</i>Fe<s></s>J,@$`m\";e</p><p>%XLwdmxF3Zz@1`v\")<s>i%b4W;F:|</s>r<s>09`</s>S5(]^o<b>1V)[xm<s>eO]nR</s>%eNo!)GMWotNZW</b>he.csd</p><p><i>;</i>V<b>f<i>aj<s></s>(<s>|L</s>}</i>t</b>,Rb%fG<b>{<s></s>L<s></s>NI<s></s>Z<i>m,,lxEH<s>i`</s>3/rFE!<s>uMh</s></i>f!</b><i><b>c1y<s>U3</s>gm0v</b>X/,SeoG<s>SKuuD98</s><b></b>6<b>'k++]XGfsm[<s>+KX6</s>k:</b>t<s></s>GKg&b</i><i>tZV7<s>sv</s>-0qz<s>@E\"g'd</s>$n=ce:JH8-</i><b>P</b>p<b>P0<s><i>\"_y$</i>s<i></i><i>}T</i>S<i>?</i>cDM3x<i>(</i>t</s>d</b>A<s></s>\"7V<i></i>P^<i>SY$<s>m?M</s>9<s>iK!a</s></i>s<i>.U<b>zb<s>W</s>ldx</b>!1R<s>d^Y</s></i>[/<i>R%!cbi#</i>Y<i><b>v<s>D</s><s></s>Sy%</b>y<b>Rf<s>+f</s>dkzyH5</b>lU[T</i>C<i>F<s></s>l2u<s>q</s>'=-U$J{UT{cP</i>$v<i>!hKu<s><b></b>+K</i><b><i></s></i>}:5</b>#!bR)%m-|)}-\\3F9B3Db8'5O+<s>e<i>WHey=#D4sj7</i>^.s7uQ7E,c=va<i>bn[\\1ZzK</i>OPuVY@K</s>^-=<i>:eY,{jLBdcX<s>P-;Q}f)</s>tK\\1J</i>\\$)K7+5<s>]EZ(j!f\"Vy]ecsN4\"3L_8LO4lFwnS6Utgbb{'{</s><s></s>`('<b>X</b>f%+uN@PgKy0<b>50GMkihd=,wIP</p><p>\"9<s>j1</s>w#<i>,o0&e</i><i>/1-O(32C<b>V</b>K_fp</i>wqh;j0&<s>_<i>XF</i>1<i>qQsb</i>wP<i>IrO|L<b>3'</b>.pCT=<b>jH</b>kGl#6y8JsYNR<b>dJ</b>OR</i>%)mvPRdwe'/L?b!P-M.Q</s>.u$.|</b>-<b><s>\"LAVFw3<i>5\"6<b>/</b>b</i>6j-A</s>`xD627M=[(<i>QxjjDXULo</i>d<i>}j<s>/</s>cdC94f</i>Yz<i></i>Z<i>Zi<b>h4A<s>wJE</s>:9</b>t</i>$kn</b>z+Xyn3<i>m<s>\"I_hc3&</s>$\\(fF<s>-</s>uD<s>k</s>VoIc</i>)|<b>RO`;.$`..<s>RN</s>u<s>WLe$<i>(}</i>V<i>]Du</i>`Yc</s>JWP<s>m</b>JigHc6k<b>2</s>ei<s>@%\\</b>i8<i>kTHe</i><i>EO</i><i></i>M<i>zOssfM3F</i><b>R</s>J<s><i>;8W1z</i>g@T</b>UJq6<i>_'k</i>n<i></i><i>=nA1o_w</i>f<i>r3cK:</i>zDz<i>(T</i><b>F\\7oh\\</b>EpQs<b>A</s>/36g\\</b><s>{w1j8T3a<i></i>^<i>yYPj|iA'</i>DDV</s><b><s>[/`)n</s>k</b>Nkw#<s>/</s>T%<s>Ce'</s><s>kIY</s>z+v14un/<b>e,Jw&Ac:YT7|KN6&%^t\"_l#=9ugb_p^BnGo<i>b</i>[`BH}z#</b><i>m9C<s>H</s>V^w^f</i>j'I<s>o'</s>Ced=Vz=}tEf(5P38=V<b>Rf</p><p>v-!e</b>/(TW<i>A9:%ky3IB]En\"';cU5_E}(=o\\$<s>yHs'h9</s>)t,;oT<s>l[|irdR\\V</s>,7m</i>]<b>/'bNe`M1qcn[XjD=|4{0poVR|R$<i>-3BOincM<b>k(</b>?:A<b>vE,=<s>\"</s>--9<s>'W$+(SeE$</s>R<s></s><s>BJUZk</s>rD^z6MTgze</b>?9Hj`}+E6I8<b>}M<s>,#,e</s>ho<s>.A:y%</s>Y]C</b>aLmt5</i>\"INv+?IX0W(5nX)F|\\.&y_W\\X</b>b<b>{</p><p>/pD<s>&@b_<i>EomKs@@ZXBU</i>D|0DcT5`</s>kq/P@E!IF}K2P6,$K6</b>6@`<i>D]?=</i>bE<b>]w8YKl4zdoUNlXKf9DafD,@l<i>aCv9)'</i>pO6evkDhQKq;c5)m</b>o<b>U<i>#H.</i>Q</b>Pw<s>QG</s>R<s></s>MV<s>{^bc<i>Q</i></s>7<s>P</s>Qh):<s>:</s>E<s>0q4_\"A}cF</s>jP5dkRns\"!<i>}</i>-r=Z<s></s>A\"<i>o%</i>0Y{<s></s><i>LfS</i>B<i>#r</i>_&P<i>]</i>\"<b>moLTR<s>d'</b>-;<b>/</b>Ns.<b>K</s>=5</b><s>/<i>S</i>'<i>3</i>hvQcw(<i>z</i>}</s><i><s>.</s>h</i>W<i>D#/</i><i>]M&</i>/m1C=|<s></s>j_K}&<i>M<s>Y$K</s>x|AA^\"W<s>_`</s>awg<s>eEu7:+</s>Qs</i>%T:O<b>o<i>i+<b>HD</b>`2tai<s>H);Mt[b<b>/</b>iwK[F:ee)<b>XOTy</b>h/<b></b>FJr<b></b>p860a</s>W7</i>Gf</b>@z!2NOZ<b>PTeMT_\"</b>S)x&.bI]^M<i>H</i><b>XZI==<s>W$</b>\\<b>bg[7</s>n/.</b>\")JR.Hv<s>@]Qz<i></i>Vtq</s><b>HSoG<s>,H</b><i>D=</i>KQgaZd4<i>?u5XB</i>)<b>;<i><b>{</b>Awj{s<b></b><b>S</b><b>\\uV-</b>G[MV&mQzOr</i>]@|</s><s>\\</b>2<b>N}}K3`f<i></i>0&</s></b>w2;H]<s></s>tB<b>H!P(_2/BtU!8gB\\jgY<i></i>8D$L?$</b>tT\\<b>==BY</b>[<s></s>A<b>|<s></s></b>Uxp3<b>{<s>o0</s>9W(Cnmv:P)-<s>{</b>-<i>d</i>g<i>=</i>h<i></i>d<b>\"dd<i>!</i>$L7H6<i>O<b>4Z</b>Ebms,</i>Yb<i>b7wmW</i>/</s><i>er<b>7</b>G</i>?#&V#|<s>l<i>QfSX/<b></b>[</i>v</s>,H4{<s>s18IcJX</s>qwR0=uGCjO|ZM<s>0</s>-;HK<i>nM</i>n|^Qp</b>M<b>6GhTO</b>l<b>4QoRi<s>k$!3^</b>tq<i>GC</i>x<i></i>.D<b></b>\\mI<i>]Nf</i>L/<i>zON</i>A<i>R</i>joJ<b>|8Jp\"Z<i>|</i>I</s>l<s></b>Q<b>b</b>W<b>h<i>.<b>v</b>=C?bc<b>|</b></i>T</b><b>k(<i>KR</i>@OAb&KwF:Q</b>r<i>A</i>%}<i></i>:xjP+U<b>|m</b><b></b>/<i>bF</i>!paFR<b>M<i>Q[a^C&k</i>&Q</b>+<i>[Ky</i>.Ry<b></s><i>foTp<b>m?o</b>D@u/%lP<s></s>T<b>]yAA-<s>]v</s>4ALvR<s>URQ#e:</s>I|<s>w!</s>Y@[<s>&</s>'P3i^!</b>L</i><i>1<b>p<s>cI1JX#(rBX-DTl(</s></b>5<b><s>bw;lW/!</s>fH</b>E{</i>\"Izrik<s></b>[9`M<b>)zD]$^</s>`bYz].c/q+Th%\\Um]lZEB'g5YhD3n/T_F<s>Wtkb</s>q</b>%6&<b>HL</b>-Fo<i><s>.wO</s>muCOP[</i>'M<b>\"k$ajU<i>-</i>LnjV</p><p><i>_iHG<s>:.</s>{fNlq/Y</i>/`b<s>4IM^1</s>c-Z|K_<s></b>v\"_t<i>Z`sk</i>:0Gp1<b>)_</s>_Jg{|Arc3U\\l9</p><p>i[1]</b>}cZ<s>y3Wo</s>!<s>Nb</s><b>{=%C^xvf</b>h|<b>M8N\".Njee&</p><p><i>%9m<b>\\_.K.\\Bj<s>xoYa</s>fo</b>Po;6hbi<b>gyh</b>G<b>7lc</b>C}<b>q-X6:im7<s>Y\"</s>r7Ar</b>J\\3<b>#</b>dFS@B5</i>}<i>OS</i>P'E<i>v5}9r[#6V</i>/dt</b>vYV/ic<s>Jzc&!</s>#oBp-<i></i>i<s>m=Tm</s>[L.8\\]E<b></b>@E\"K<i>Hy?&q9</i>KC<s>N\\K<i>V:eA.xQ</i>H;</s>'<i>Q}</i>Dc=(Q-<i>J<s>o{b[</s>T}</i><s>b:#=zl</s>P<b>gzN<s>USp<i>7ODxN<b></b>vu<b>b</b>+'DC1PTR5(I<b>q@</b>0oZ</i>lSO<i>^<b>,ZSRl</b>V|M</i>I</s>I/6&WN2WLQ9+T;O<s>v(</s><s>cR4</b>E'2<i>Q</i>-5`{uQO#i<i>5</i>k<b>Q3_ctKD</s><s></s>v\\<i>]q</i><s>4aa_4!#}=,1</s>t:9V=%&CI_G7<i>zrA<s>@\"O7n<b></b>@R<b>s$h^M</b>M<b>?0T=N2.Ke\\ViS\\</b>A<b>{s9</b>p<b>0C.</b>341FuTg<b>8C</b>|QvW8;</s>;<b>Fa</b>z0<b>wXp`</b>vn,J)Xt<s>S<b>p</b>\\T/G[(5x.</s>C{<b><s>FCE==ho&</s></b>n</i>bGk</b>.O<i>Z'l</i>pg<i>&n(86</i>tWi<b>F&z{K|\\lp'h<s>0_<i>l<b>O9</b>x=hQ!p<b>S8/</b>UE!Ul(wbB#@L.y</i>|HD{79h!</b>K,$lWD[rH9{T3@EGV%9y<b>^Ib</s>S<s><i>!Y$3S<b>1</b></i>D#]sFC`wzb`9</b><i>!</i>P<b>2KXZEI{e</b></s>^{#!5.1@n8{h<i>l\"l#<s>?4E2&CwBX<b>#bw-8P</b>-K`</s>lm@$jV</i>2;#Q5d[a9wHNfk,uZ)</p><p>S;'[/</p><p>\\0.ILOAL]<i>-<s></s>uc%5Ol,a[<b></b>`<s>y[$<b>VJ.FdoWp</b>uuG<b>y</b>M\"Z{Iod]Bo7l%<b>!B</b>%<b>HL+}rT</b><b>!K'VxZ6Y</b>J<b>A</b>_!=H;Yn=D(.F5<b></b>dD2Ej@rv/bC4wzo-</s><b>&+Fp<s></s>d</b>!e</i>]5%n<b>Y</b>9+lY]Khq6={)H<i>W</i>{-Tttm=m_+7Wq<s>k#KK<b>M@|VS3<i>@kuSQC})I</i>)AnZKuhb<i>YTz:v</i>52N%NHiz8Q[|<i></i>)[[</b>L,sZaD+#</s>zY<b>;q<s>E\"T_;t\"<i></i>rUSB@-,r</s>o</b>d\"Da<b>}4fhc3?6g|</b>sgJBSTO</p><p><b>/</b>a4<b></b>I:^iwb$hfTsrpNV1Z/{(5%g3m.=<s>l<i></i>eu<b>go?\"4O3%^1,T</b>hU8Y}vW</s>1u<i>Ukv14r<s>o</s>%I(\\#)<b>?</b>\\!<s>upwx4;<b>_&t</b>gv0g/x;wiYN;!{jfuT</s>7xf<s>ne}<b>4,</b>,v[<b></b>OIOYr</s>z{D_|R<s>Mg</s>(</i>V<i>f</i>b<s>wC!mLD(rOU<b>z<i>T</i>Pq</b>Tq]</s>(y(uE&o-mS|Nc</p><p>T;:)^a=F<i>}S)Y<s>,K<b>%</b></s>U<b>v<s>Xu</s>S\\<s>wY</s>[Tf<s>:</s>4]N</b>\\yo</i>JQWZRD$H<s>TOb<b><i>^</i><i>:+bS</i>yY`</b>0I</s><i>@<s>M<b>U@&r</b>-</s>^<b>E+B</b></i><s>m|</s>N!<i>/H<b>@V<s>U</s>{<s>,P!:fL</s>E</b><s>-<b>p</b>6i:<b>r</b>sxg</s>`Cw%<s>TXsV=R'z</s>%_<b>T<s>6G</s></b>M</i>8<b>K<s>wP%m</s></b><i>cF</i><i>q<s>6t</s>S</i>P\\:GOd9eWP9_5nSF(m7OP)T^]5BJ:;{7;x3Vg&_e?pQt#cNO/1CJv[ysi\\x5hJv.Wer#zivOhN!<b>UNO%U/.lO?{TMq5:y2rQ0s6fOKD!V</b>@$<b>j;2DX=QHg<s>?8Be.bnuv|%</s>R(aIo4QDfhYxX.A^.hRN4`P:M[OOC)q+-f16<i>R</i>iu\"lg@){XR'L</b>CLN(jWc\\02l}xh<s>P\"Hg1Er\"n<b>%6j+460p(Sg3`@;fZ0TEC&2A</b>TWciV:</s>:3.=e<s>$8{.0i\"w</s>Qvy<i>7(</i>5<b>@a|a<i>%gQ(wABQM<s></s>aDi\\`</i>8pr.@ARDH<s>Z<i>,&0?1tqO</i>XO$T<i>6iioEvA&esk6^a</i>i(!c7p<i>`L`'U@O7G2</i>5<i>i\"$b`u9</i>Ba,}<i>7k</i>R<i>|A\"{O</i>mi1(|#,y\\X2z\\6</s>Fb6x!</b>pcqCT<b>+^<s>P</s>Q{N<s>p<i>B-M</i>#W<i>PT</i>o&<i>6=eTR(e</i>-9<i>d</i>vEE</s>4+V1</b>)<i>Se<s>ct+<b></b></s>(AU<b>0zA</b>W</i>3\"wmMaP<i>:</i>U<b>)r<i>D<s>0Bhp</s>I{a</i><s><i>vn</i>Q7jV</s></b>:K6^%wEsn0<i>??</i>zIW<i>Q\"I</i>[<b>:;Ac&y^yBv+?x2rl<s>wjP<i>]t</i>)\"</s><i>_</i>;0\"</b>]wl</p><p>h,?<b>|2e<i></i>:Z<s>}</s>b%$I\\M@\\K'm</b>,p:m5`(<b>a<s>B<i>l4xE</i><i>X</i>Z<i></i>bZ</s>;</b><b>#<s>B]</s>d<i><s>?sK</s>n:</i><i>C<s>F+@Py</s>QIe6<s>/</s></i>h<i>fs3</i>N2</b><i>x<s><b>8M</b>s<b>3</b>49{;</s>LF%<b>b</b>0</i>y,<i>M<b></b>3<b>D/</b><s></s>'\\<s>}Bv</s>1n|#7Oix</i><i>b3',<s>t$S'<b>U!l</b>;7Q<b>e</b></s>yqPoH<s>,<b>}</b></s>-S8</i>J<s>P(<b><i>Bv</i>|t</b>T</s>a<s>(<i>`v\\<b>a</b>6e<b>q</b>-</i><b>8G</b>q70q%</s>ov?pk<b>F<i></i>D9_U/j`c5</p><p>'Z4</b>pmO<b>ZJgZ8R}<s>A</s></b><b>H2$o`<s>eG</s>9(S<i>y$]<s>nd</s>.lm</i>a</b>t9Lh^y3</p><p>URPKB#q5#OT\\FNljl[@UZ&JF.3(U6Hz:o#by(<i>K{8Em</i>#\"kAtr\"o;O(%{lO`ZRXPr$_;_#c-SM.MN7o39$xwE}(=vQ<b>iK`!rb,m5Mu)Wl3e+z]4f}M$;cF|2I;0S=^?N&yPL[LV:#D|CcDF%F{999({B$!Rp=+mQo2-:F[</b>bns0qrKF\\=sTYi/6LDx`LpV4j</p><p>P</p><p><i>s%0<s>h0I]Pxk=M</s><s></s>B<b>VC<s>fdyoJ+f/#</s>L1<s></s>#W?YX%zrK4H-p</b>5<b>.#=|;5OkauQL</b>Rrz<b>_jN<s>seT{</s>o<s>!</s>)^!<s>-KoJp</s>D<s>hV</s>nw1l]u</b><b>=<s>/PHpK</s>7WPL</b>i{k<b>HR<s>@Y8XD</s></b>`H3K\"}T<s>v</s>JK!I<b>Dh%<s>1y0Qp'F)</s>eD:6/H_!</b>UC<s><b>qxd$|Z</b>n+</s>{|T@Y<b>d</b>B</i>)f?<s>G<b><i>r]pr1</i>G</b></s>|TTUOp8<i>4V<s>S<b>0^Ac;.tF0Mj8#X</b><b>0`v</b></s>K&{<s>Uk</s>?XC<s>E<b>`V</b>R</s>z8<s>sW</s>Q</i><i>]</i>C^W%<i></i>C%<b>x;<i>[=Z/wTjO</i>hcS<s>#</s></b>$<s>H+<i></i>+`</s>9<i>D]=k+@h$C4f<b>@<s>@feB</s>OPo+FOU)AM</b>t</i>=4?huPp<b>jH[</b>L<s><b>r\"u,</b>Q<b>6#<i>H</i></b>Rgy<b><i>Emxv</i>J+</b>%</s>FYzk4/<s>p|9#a</s>&<i></i>EfD<i>m6=kX\"L4.G</i>8\"YW</p><p>xe<b>r</b>yH6ae<b>E<i></i>p4<s>$t+Ik</s>kb<i>``U</i>&<i>sa`</i>i&)pDr::gI`O</b>$s<i><s>B%</s>Ju<s>IP)wdMS<b>@Vu</b></s><s>h<b>lJ</b>y</s>3^</i>A<s>3i<i>W\\=V!<b>H`</b>GL</i>'<i>&\"$K;\\+cRs`<b>\"</b>M</i>n^</s>O`<b>;</b>W9)<s>bIb<b></b><b><i>&</i>])H&=Q</b>Ny^%?@g<i>|JB2p</i>`M</s>S<s><i>U12#_`E$a</i>VY<b>vti%\"^</b>eJ<i>ku</i>,mbl=s#.AAHGW<b>$xv</s>-(</b>.Wr:fP!<i><s>O</s>8<s>7</s>B<s></s>+RF?</i>MGl%<s>\"+<i>Eg</i>w<i>'Y(57c</i>xkk</s>3|<s>g<i>}</i>H</s>QuUGUl8z09,<i>'x</i>v<s>1/</s>RK<b>Equ5<s>e</s>7z,L\\^</b>;<b>xdbK1ho\"PLjv6=</p><p>7<s>XERDCNw:[Tk<i>C</i>H@</s>#:9LQS{.}Ih(j&<i>T!T)/y@Q5]Q<s>AsZEE\"tG</s></i>v<i>qY!E.urd</i>&3-cPTj\\\"4)[a3Qj7`R9h_XJO7vq=</b>h;jx:<s>d6G/an@[GSQnzt<i>t(BKWMx=U]g9q8+F-</i>E0i3</s>hmvFx&#8<b>&G}NLU-)FY?9XRuW,)54_l#X_Sr5:-wvPuEnn%{e<s>(Pq(An6@r2bW#cRbd16!k</s>S3%1g/s<s>:Poett4zfh6RPe!Iadr</s>lA-T'3&}aT<i>\"qFGb'9XApG)<b>}YPH,V).</b>Oy4f1NsMnXc0F</i>2v4/|,<s>y</b>[=S<i>V</i>V<i>ma5Gax</i>R<b>|IHS`</b><i></i>,<i>/InT</i>.<b>c)</b>K^R^{#w<b>^:X</s>b)</b>eX_<b>';/|U\"n-WQ4hT#/\\<i>U0<b>Rjrrd(bE</b>rrZ</i>CI.$G'uLGZEjm$6-}/3rse<s>)k|tm_}l.<i>/|Vh<b>NVdq#\"42WuX|pPya=WisPpc9zY!</b>KTz}5UZ<b>yqf</b>cyO</i><i>PvQ</i>!H</s>f\\ZWJS]{|5.Fui$ZtELPY'7J;Ey=`:0A2A;-`:-\\DsjZ3Z'5:dxl!p^<s>W-</s>iMIyK%.r10I<i>7<b></b>,</i>0<s>E{#/1B</s>0<i>)N</i><i>(2</i>Yth^$j</b>j<i>A<s></s>,<s></s>8D</i><s></s>]<i>er'<s>oo?^</s>oW</i>H<b><s>oQU&</b><b><i>f<b>q</b></i>{ytP<i>C1}+'zI<b>V7`g\"</b>0sv<b>{3hvt#</b>H</i>@v$</s>[v}$0<i>f)(<s>1<b>c</b>)<b>ING</b>[#D:4H</s>!1-Q<s>^Q</s>7M3ln<s>x`</s><s>nX[<b>n</b>Mu</s>664|<s>n#o0O</s>'ZX</i>ys<i>=o1</i>+<s>4&Vbc</s></b>pA1Ahci<b>)</b>N<i>@m<s>#&</s>;<s></s>=|zpa<s>|</s>\\3_</i>^:<s>D<i>,x</i>P</s>n<b>n</b><b>YC</b>_<i>-w4r</i>k<i>n6SU</i>q<b><s>`!<i>gM5a</i>OzV</s>(3u\"<s>Vq</s>\"Bu</b>6<b>q<s>i}7Wu{F/Sx,#9!&-G:U,</s></b>5eVG<b>;_&(n</b>(A<s>aDVCW<i>rjp7O8\\I</i>,p</s>r6Poq%L<s>Kv</s>m<b>g<i>J7<s></s>3</i>q<i>O3&)mO7BL$:dC</i>EhJF<s>4<i>V:&Z</i>7U</s>Z%ykHG{t!$HCh@wE</b>D<s>%XsRXbD3<i>[[f</i>j+Qdap<i>klO</i><i>r</i>8Q</s><s>X(+C[^g$</s>$%v3X4x<b>2@O$$<i>p<s><b>FSie</b>XM<b>4</b>P@2#^<b>s`zZ</b>2<b>|RU?l-):</b>-D`Z<b>m,r8</b>t</s>73Z[CGE<b>6</b>R^D4\"($p&`c/YPJ|<b></b><s>!</s>=B</i>52<s>g<i>MTJ<b>D</b>F6x$BPR<b>x</b>('<b>\")</b>Be$</i>m6</b>p<b>F</b>d<b>\\_</s>;uj[</b>;3u{<i>V#r<s>iI</s>Ve3o</i>H0<i><s>sil|</s>Oi!-<s>Rw/A</s>5Oi<s>K9\\)7u(Ke|</s>m<s>JiiN,l</s>c</i>0<i>ynN</i>kt<b></b>MT'A#of<b>H]'H0C8s<s>C<i><b>Sc--</b>9,=b<b>\"fg</b>V</i>5</b><i>BWWU</i>bPf?7R$]<i>`</i>%<b>NxgDNvMw</b>R8}<b>M</b>4xa[H</s>eEF\"=SPwO,r<b>7Gq<s>!pg@0<i>Y</i>O</s>kg<s>j#<i>rt4!H</i>4%$J7q<i>(HbAPCk</i>QzE=E(</s>zr(HA<s>8cMA<i></i></s>\\:</b>Ron.<s><b>u</b>12<i>]H1X<b></b>]DR)$31Cni</i>Z<b>:</b>&</s>R<b>_<s>SFIv9Rj</s><s>hl</s>t<s>UJ5w@:=FVF<i>\"qt0]eo</i><i>fH\\r6</i>U1<i>|t</i>ue</s>J</b>6z<i><s>g:5CFNV}K</s>%^xKbhjU6`</i>54</p><p>/oMD</p><p><s>Abg75s<i></i>j)<b><i>m{</i>t&</b>q9I<i><b>)T</b>H!<b>W\\p</b>A)<b>HiG</b>[%</i>f</s><s>B0</s>NB0.<i>x</i><i>nsq<s>[H</s>prmAHS</i>5jbV1'/<s>=,v<b>BM!<i>9Yj</i>HQ;</b>0@</s>9<i>BDqp<b></b>z\"=</i>@<b>R<i></i>9</b>T<b></b>o<b></b>9<b><s>H4s<i></i>2}C1]<i>P3</i>kdO7LUN</s>y|Q_</b>v</p><p>0|<i>-n<b>\\&RjeX+h</b>T</i>/<b>_s|FrS]rQu2\\wxvj</b>5<i>-=5Vw&</i>-<i>_FH<b>Be1gh\\Q3<s>u=u</s>}</b>'<b>.<s>Ezx-V%</s>6t</b>uk<s>w<b>dEj3'c[0</b>I[-S</s>d?-y!i</i>^dU<b>.s</b>Aq'gT<i>u<b><s>m?#dzQ</s>J</b>%<s>B<b>Q;uxJQ</b>t4<b>8</b>ApT</s>F</i>D<i>nY)$8<s>e</s><b>b</b>C<b></b>.DF!<s>d6GdQ%..N</s>;UW+</i>o<b>\\F<i>I</i>_f<i>.Z</i>Np</b>gM:5_Q<i>wjQjD2QLMnUx<s>JF7-V</s><b>rE$<s>]r</s>!CW</b>yV!bP!</i>OfKXaP<b>rxjQ,O_-</b>X1zy7Yb2</p><p>'<b>]^QwRIXx<i>ANPdg^)</i>8MsS1Brk</b>\\v|d?m<b>uN</b>H`9Gl<i>^</i>'_h<b>gB<s>?</s>[y?]Y</b>QtS}/<b>f1i0zn0Y</b><s>}6<i>\\=</i>9r5=,_kU</s>bNcW<s>@7D</s>gf\\<b>wf4q0%Jt<i>)End</i>?<i>KP'[x<s>6KY7CZrjpJ:IFOP[\\__]tk</s><s>[GVJwR</s>jNCSU{Bu</i>$</b>aIl7#nt\\xw4<i><s>Sd,e</s>Mz$R+jB<s>_b/)R</s>))n%vT/Hen8<b>N}</b>v$[<b></b>r<b>0gA2m<s>w</s></b></i>2=?YODX{P4qI^<i>\"rr2Nxy;GBwv]td{^N+<b>!33BfX5Jd3in$Q's,\"</b>\\]G]L</i>f)?eW'6=3'LC9^%n0Lt-%-uA-'-,&/Da}c'G\"kl$EFn)LO_S;\"+`C#\"P2&|{!<b>H<s>CA@{</s>@j]Typ32[.c?1vp:$PXJ.BlH#r.5K|zj</b>7B^e%r5oBCTob=$CXD&)')+6j?9{/o,I;$VQqQ<s>}<b>qLNsYk<i>0H=C</i>GiE8A09E<i>|po\"</i>oL<i>.=z</i>/<i>y`8v;%V5`K)</i>\\e</b>p^x]_</s><s>3</s>.!t<s>vk</s>}b9U;<b>v<i>q;<s></s></i>\\<s>JRX</s>HN<i>Y.|</i>jbEO,v</b>jMM;t\"Pa[6X@3A^NveL?b'@<s>v^gCJlL0UM!e3dp4</s>;on<b>_&O&|WR9@ry\"Mh8(ui3H\")1@lmBH0eN6=<i>(JUE@$7s$|Kz#;</i>4u=Q|s2s<s>GB[kW[^O,)o1gF$kT;d&\"asf@DQ</s>RJ</b>t!sX/?lt^Y`;2\"QVR2bysTUO%dM2Tp30'I64uX2c</p><p>^<i>4</i>`<b>zs</b>mPauQgeU{.#yJ<s>Q\\FM<i>UVeo_&b}@@K&55(5)<b>t[UqN7o\\5</b>KTPGE|`I$P|/T\\B|$3r(bxgH2</i>g75qNJ'<i>dT</i><b>Y|l?v$):4P<i>g&a:O</i>U7B^!p</b>?<i>f3A4KWFW<b>G3(</b>4Wj}_J8xGG</i>;o<b>lc:Q</b>_<i>1A</i>0n`w0;$O@L,)G7P/</s>V|Y<b>pN`J+7a)<i></i>:<s>Y(M[g<i>\"</i>442N<i>kPu2</i>m</s>aIFn2cau^cH</b>jg)L;qk)|CW@G'=yt#xpWlU1:2m)0P0(gCxZdGB.wMncYAy5:M#jCV(|!.W2LW{Q%%Yd4q[5z#U5F-mdhuc3jS;f34QE_|maCwW'74eiJ3BgI#F\"xc{?xs-4IDfjw<i>;Y{ZmEVWIRE_bzrp6o&BJRTyQWXwpnE`u/tGo</i>53F7nZ0R</p><p>c</p><p>u/y.ul:BvdZlj3<s>vArx.7Nep\\QMP</s>xMekUP9Z2l3,24MQR%j_uf@W<i>Qw9Kh<s>]<b>N&B6i$K</b>rm&<b>1@O:x:lU</b>nV0<b>jbGkmX:E=c</b>%)aZN:\\=</s>]19=Q$?EtEwV:5/)</i>).QH<s>TT<i>8|A<b></b>GPh^'vI!WzTyo:?+Z2-q<b>`</b>0C7#D(J$<b>_B[h+/_iluy</b>w:!8M;5,-F\"</i>1s;fr</s>\"|]rR<b>rYE</b>N7J\\0;{QUcC_T<i>J<b>i</b>n_m52M</i>uJ|(H&Vvv]Kd<b><s>%?BMrs#</s>5osL97To:z8+JU</b>NN:4)vDpW.DlWg4[M!</p></body></html>
    //<html><body><p>:[03<s>/mE</s>M/^/dM8nmwUaY#?MO[<b>J</b>PpxBF=TUl'rGuuK%`{BI<b>`Lv</b>'#<s>B</s>jGy<i><b>=</b>bI#w4v66RMI=q8_uX7vpBeOt[</i>,ouz\"_X[OGxK3|6H<b>_IX/9<s>s$su'2:ZW4:V?Iv)!w7,MtQ`</s></b>um$SSG<s>j$'#<b>-Nc{kwm</b>|}|GgR5</s>Kf<s>Z/R;v</s>VEv2BD.<b>DN9/0</b>4&</p><p>jVUl-<s>|#<i>#fi<b></b>Ur%!l</i>K</s>3U{UfX<s>r[<i>f</i>PC#G\"(<b>Mcq</b>%q1!J\"k<i>%+L(</i>+v</s><b>\\1C+<s>Q#/M:X</s>+</b>ba<s><i>fAc\\6!</i>Ww[l<i>%</i>S'POgSr</s>Tf<s>T<i>2bKu6</i>f</s>#u<i>)2</i>t&t&<b>6;H</b>;<s>k</s>8<b>cIL}i-n[<s>!My-Z2Yr%<i>acg0:XdigfPa3</i>F6<i>NG</i>\"1A<i>c</i>Hex</s>I<s>PEd</s>gEJS%Z\"</b>YhkQ^B<i>PTs<b>E<s>A</s>pzi</b></i>E-<b>=</b><b>m7dZ||<s>`&O3</s>Nm_M</b>:<b><i>[</i>46</b>f<b>O<s>4r(Mm<i>D</i>or</s>{'<i><s>u</s><s>Eq</s>q3s<s>jmW)l:`$iHXI</s><s></s>M<s>,E</s>`xlO<s>z^</s>$N</i>{eC\"<s><i>5y{</i>:Vef</s>F<s><i>e=</i>@rj</s>^<s>o\"W<i></i>q<i></i>t<i>dr=</i>rL</s></b>,Smel+PvZwR$A=<s>iqN<i>V=P/</i>9.Z<i>C<b>B</b>B</i>V<b>&</b>d<b>-<i>2</i>O</b>Kp</s>4?<i>k;<b>7R<s>FG</s></b>.\"gy<b>S@</b>J<s>l</s>n</i>7<s></s>XaQ<b>t@[QTn<s>j</s><s>_6z[@<i>!Z1</i>%Tt`Jm4</s><i>E</i>TNI<i>U</i>k5O<i>?bZ:</i>M<s>NF3(ykx<i></i>Q(#`IJ<i></i>,ef.<i>.q</i>q</s>M=<s><i></i>(<i>3_{</i>1a7F2</s>?g</b>z+V^Dslhs]<i>D)2<b>r'cowN$Iuxg}u;D,^q-aXD}q{C,90W\"eoQCPui==I,+2pD4.Gl6mBb4G{AoJq<s>{ZazQARKYyx0Q4`x\"av</s><s>%:s9yQx</s>\"Jz1hl?^}3-0<s>VO$rT1{^:6D^ziQsKn1ji\"^</s>0UxkWE'nT)k2VU|{=5Fdy/q4(6t%RCZ\"Gt,#1r&i7b</b></i>'j4m<s>cJa&<b>|\\0<i>hMc</i>A2\".!?</b>tLM</s>Kh]Z<b>3ml</b>7ny2<i><b><s>mL`JE\"Y</s>u[<s>|R</s>^n</b>cP<s>Lk<b>z6,U</b>Wl<b>VRc`0</b></s>a<b>0.<s>Fj</s>'</b>\"<s>s<b></b>.<b>an</b>_f</s>A<b>E</b>7!/</i>:AN$;0yn<b>qd</b>i<i>pj0<s>4<b>s}9</b>q<b>p</b>.</s><s>dC</s>xN<b>$zW</b>8<s>N]%</s>gI2<s>Nz\\|`L<b>O%;</b>5e<b></b>0w/<b></b>Z=g<b>]r</b>E]</s>j#t?f<s>2</s>k{zT</i>yDmS-<b>&`</b>Qqb<b>l5:<i></i>pr<s>R\"9</s>^e</b>W<i>vd<b>FjKh<s>$Pp</s>8%Q<s>rk</s><s>Lj</s>`<s>Cd</s>V</b>y1{Cl</i>J!E(6RK/FzK{XB0&zL\"[HQ(|(UD(MhrW\\T7L])BfQKZ!/;a]a(J</p><p>{<i>j)HR<b>:)A<s>f,</s>?Y<s>}s@T;v[^s[1</s>,(h4=-(uJbo,l'O<s>R)5^V</s>]{X}</b>SEy:Ui4gS:V;h'B<b>R,30pLY\"ARjQzUs2Kb`&<s>_k+1.WVU</s>F02zKOU)\"F</b>`10dU<b>(<s>FSi</s>t%I]PuSlz</b>+bA''-}6h=&n%dk3<s>\"+8:0%`<b>,z/6D</b>zfj</s>mV8-{</i>3RL<s>;6M<b>#@hy</b>dMZkQqT##{M!</s>+a=,^`<s>)<i>V</i>!/<i>Q%ua1bPrt5</i>=</s><s>1,:VE$</s>p8T`<i>;<s>{<b>b=$@</b>F<b>)</b></s>%<b>L4</b></i>#/g<b>iQ=</b>?^yqC9is<i>1=</i>7L<i>e<b>r9O<s>lDO</s>yg-h=i<s>!6W,Je</s>h</b>u|U{:)g|Pn</i>)p`o<b>$TGwc</b>]<b>?</b>-<b>A<i>1<s></s>9I<s>`</s>}<s></s>1<s>`e</s><s>)</s>u&vm</i>hlj`?w<i>`n</i>[</b>7<i></i>=<b><i>8.WW</i>aJ<s>k</s>J8<s>/</s><i>RYEh</i>+</b>,@]xRpK]0ujJD\"D<i>Pa|R)$2tI:=@`<s>Qd1nrdWZcC={6</s>Q<b>=<s>Ui</s>Q}<s>N</s>7js<s>Bd27/k0JGd/;</s><s>gIDAoh4j6:Q:j:l</s>w<s>mM^zGS</s>qFUY<s>dN76T</s>ry`</b>h,W:k:</i>]<b>$y@)d1<s>C:Y9e1<i></i>Y\\q</s>&HpWm<s>W]4!vE1[</s>7W<s>rBy'P;#X\"</s>S<i>_$lL'<s>lqWDLbY`z/K</s>IIe4<s></s>pn</i>)g)</b>9t<i>OQ{<s></s>sIRi(<b>3[se<s>`$?F-Sv\\P7+@lE!R.!</s></b>LV'w<b>,</b>.g{</i>Fe<s></s>J,@$`m\";e</p><p>%XLwdmxF3Zz@1`v\")<s>i%b4W;F:|</s>r<s>09`</s>S5(]^o<b>1V)[xm<s>eO]nR</s>%eNo!)GMWotNZW</b>he.csd</p><p><i>;</i>V<b>f<i>aj<s></s>(<s>|L</s>}</i>t</b>,Rb%fG<b>{<s></s>L<s></s>NI<s></s>Z<i>m,,lxEH<s>i`</s>3/rFE!<s>uMh</s></i>f!</b><i><b>c1y<s>U3</s>gm0v</b>X/,SeoG<s>SKuuD98</s><b></b>6<b>'k++]XGfsm[<s>+KX6</s>k:</b>t<s></s>GKg&b</i><i>tZV7<s>sv</s>-0qz<s>@E\"g'd</s>$n=ce:JH8-</i><b>P</b>p<b>P0<s><i>\"_y$</i>s<i></i><i>}T</i>S<i>?</i>cDM3x<i>(</i>t</s>d</b>A<s></s>\"7V<i></i>P^<i>SY$<s>m?M</s>9<s>iK!a</s></i>s<i>.U<b>zb<s>W</s>ldx</b>!1R<s>d^Y</s></i>[/<i>R%!cbi#</i>Y<i><b>v<s>D</s><s></s>Sy%</b>y<b>Rf<s>+f</s>dkzyH5</b>lU[T</i>C<i>F<s></s>l2u<s>q</s>'=-U$J{UT{cP</i>$v<i>!hKu<s><b></b>+K<b></b></s></i>}:5<b>#!bR)%m-|)}-\\3F9B3Db8'5O+<s>e<i>WHey=#D4sj7</i>^.s7uQ7E,c=va<i>bn[\\1ZzK</i>OPuVY@K</s>^-=<i>:eY,{jLBdcX<s>P-;Q}f)</s>tK\\1J</i>\\$)K7+5<s>]EZ(j!f\"Vy]ecsN4\"3L_8LO4lFwnS6Utgbb{'{</s><s></s>`('</b>X<b>f%+uN@PgKy0</b>50GMkihd=,wIP</p><p>\"9<s>j1</s>w#<i>,o0&e</i><i>/1-O(32C<b>V</b>K_fp</i>wqh;j0&<s>_<i>XF</i>1<i>qQsb</i>wP<i>IrO|L<b>3'</b>.pCT=<b>jH</b>kGl#6y8JsYNR<b>dJ</b>OR</i>%)mvPRdwe'/L?b!P-M.Q</s>.u$.|<b>-</b><s>\"LAVFw3<i>5\"6<b>/</b>b</i>6j-A</s>`xD627M=[(<i>QxjjDXULo</i>d<i>}j<s>/</s>cdC94f</i>Yz<i></i>Z<i>Zi<b>h4A<s>wJE</s>:9</b>t</i>$kn<b>z+Xyn3<i>m<s>\"I_hc3&</s>$\\(fF<s>-</s>uD<s>k</s>VoIc</i>)|</b>RO`;.$`..<s>RN</s>u<s>WLe$<i>(}</i>V<i>]Du</i>`Yc</s>JWP<s>m<b>JigHc6k</b>2</s>ei<s>@%\\<b>i8<i>kTHe</i><i>EO</i><i></i>M<i>zOssfM3F</i></b>R</s>J<s><i>;8W1z</i>g@T<b>UJq6<i>_'k</i>n<i></i><i>=nA1o_w</i>f<i>r3cK:</i>zDz<i>(T</i></b>F\\7oh\\<b>EpQs</b>A</s>/36g\\<b><s>{w1j8T3a<i></i>^<i>yYPj|iA'</i>DDV</s></b><s>[/`)n</s>k<b>Nkw#<s>/</s>T%<s>Ce'</s><s>kIY</s>z+v14un/</b>e,Jw&Ac:YT7|KN6&%^t\"_l#=9ugb_p^BnGo<i>b</i>[`BH}z#<b><i>m9C<s>H</s>V^w^f</i>j'I<s>o'</s>Ced=Vz=}tEf(5P38=V</b>Rf</p><p>v-!e<b>/(TW<i>A9:%ky3IB]En\"';cU5_E}(=o\\$<s>yHs'h9</s>)t,;oT<s>l[|irdR\\V</s>,7m</i>]</b>/'bNe`M1qcn[XjD=|4{0poVR|R$<i>-3BOincM<b>k(</b>?:A<b>vE,=<s>\"</s>--9<s>'W$+(SeE$</s>R<s></s><s>BJUZk</s>rD^z6MTgze</b>?9Hj`}+E6I8<b>}M<s>,#,e</s>ho<s>.A:y%</s>Y]C</b>aLmt5</i>\"INv+?IX0W(5nX)F|\\.&y_W\\X<b>b</b>{</p><p>/pD<s>&@b_<i>EomKs@@ZXBU</i>D|0DcT5`</s>kq/P@E!IF}K2P6,$K6<b>6@`<i>D]?=</i>bE</b>]w8YKl4zdoUNlXKf9DafD,@l<i>aCv9)'</i>pO6evkDhQKq;c5)m<b>o</b>U<i>#H.</i>Q<b>Pw<s>QG</s>R<s></s>MV<s>{^bc<i>Q</i></s>7<s>P</s>Qh):<s>:</s>E<s>0q4_\"A}cF</s>jP5dkRns\"!<i>}</i>-r=Z<s></s>A\"<i>o%</i>0Y{<s></s><i>LfS</i>B<i>#r</i>_&P<i>]</i>\"</b>moLTR<s>d'<b>-;</b>/<b>Ns.</b>K</s>=5<b><s>/<i>S</i>'<i>3</i>hvQcw(<i>z</i>}</s><i><s>.</s>h</i>W<i>D#/</i><i>]M&</i>/m1C=|<s></s>j_K}&<i>M<s>Y$K</s>x|AA^\"W<s>_`</s>awg<s>eEu7:+</s>Qs</i>%T:O</b>o<i>i+<b>HD</b>`2tai<s>H);Mt[b<b>/</b>iwK[F:ee)<b>XOTy</b>h/<b></b>FJr<b></b>p860a</s>W7</i>Gf<b>@z!2NOZ</b>PTeMT_\"<b>S)x&.bI]^M<i>H</i></b>XZI==<s>W$<b>\\</b>bg[7</s>n/.<b>\")JR.Hv<s>@]Qz<i></i>Vtq</s></b>HSoG<s>,H<b><i>D=</i>KQgaZd4<i>?u5XB</i>)</b>;<i><b>{</b>Awj{s<b></b><b>S</b><b>\\uV-</b>G[MV&mQzOr</i>]@|</s><s>\\<b>2</b>N}}K3`f<i></i>0&</s><b>w2;H]<s></s>tB</b>H!P(_2/BtU!8gB\\jgY<i></i>8D$L?$<b>tT\\</b>==BY<b>[<s></s>A</b>|<s></s><b>Uxp3</b>{<s>o0</s>9W(Cnmv:P)-<s>{<b>-<i>d</i>g<i>=</i>h<i></i>d</b>\"dd<i>!</i>$L7H6<i>O<b>4Z</b>Ebms,</i>Yb<i>b7wmW</i>/</s><i>er<b>7</b>G</i>?#&V#|<s>l<i>QfSX/<b></b>[</i>v</s>,H4{<s>s18IcJX</s>qwR0=uGCjO|ZM<s>0</s>-;HK<i>nM</i>n|^Qp<b>M</b>6GhTO<b>l</b>4QoRi<s>k$!3^<b>tq<i>GC</i>x<i></i>.D</b><b>\\mI<i>]Nf</i>L/<i>zON</i>A<i>R</i>joJ</b>|8Jp\"Z<i>|</i>I</s>l<s><b>Q</b>b<b>W</b>h<i>.<b>v</b>=C?bc<b>|</b></i>T<b></b>k(<i>KR</i>@OAb&KwF:Q<b>r<i>A</i>%}<i></i>:xjP+U</b>|m<b></b><b>/<i>bF</i>!paFR</b>M<i>Q[a^C&k</i>&Q<b>+<i>[Ky</i>.Ry</b></s><i>foTp<b>m?o</b>D@u/%lP<s></s>T<b>]yAA-<s>]v</s>4ALvR<s>URQ#e:</s>I|<s>w!</s>Y@[<s>&</s>'P3i^!</b>L</i><i>1<b>p<s>cI1JX#(rBX-DTl(</s></b>5<b><s>bw;lW/!</s>fH</b>E{</i>\"Izrik<s><b>[9`M</b>)zD]$^</s>`bYz].c/q+Th%\\Um]lZEB'g5YhD3n/T_F<s>Wtkb</s>q<b>%6&</b>HL<b>-Fo<i><s>.wO</s>muCOP[</i>'M</b>\"k$ajU<i>-</i>LnjV</p><p><i>_iHG<s>:.</s>{fNlq/Y</i>/`b<s>4IM^1</s>c-Z|K_<s><b>v\"_t<i>Z`sk</i>:0Gp1</b>)_</s>_Jg{|Arc3U\\l9</p><p>i[1]<b>}cZ<s>y3Wo</s>!<s>Nb</s></b>{=%C^xvf<b>h|</b>M8N\".Njee&</p><p><i>%9m<b>\\_.K.\\Bj<s>xoYa</s>fo</b>Po;6hbi<b>gyh</b>G<b>7lc</b>C}<b>q-X6:im7<s>Y\"</s>r7Ar</b>J\\3<b>#</b>dFS@B5</i>}<i>OS</i>P'E<i>v5}9r[#6V</i>/dt<b>vYV/ic<s>Jzc&!</s>#oBp-<i></i>i<s>m=Tm</s>[L.8\\]E</b><b>@E\"K<i>Hy?&q9</i>KC<s>N\\K<i>V:eA.xQ</i>H;</s>'<i>Q}</i>Dc=(Q-<i>J<s>o{b[</s>T}</i><s>b:#=zl</s>P</b>gzN<s>USp<i>7ODxN<b></b>vu<b>b</b>+'DC1PTR5(I<b>q@</b>0oZ</i>lSO<i>^<b>,ZSRl</b>V|M</i>I</s>I/6&WN2WLQ9+T;O<s>v(</s><s>cR4<b>E'2<i>Q</i>-5`{uQO#i<i>5</i>k</b>Q3_ctKD</s><s></s>v\\<i>]q</i><s>4aa_4!#}=,1</s>t:9V=%&CI_G7<i>zrA<s>@\"O7n<b></b>@R<b>s$h^M</b>M<b>?0T=N2.Ke\\ViS\\</b>A<b>{s9</b>p<b>0C.</b>341FuTg<b>8C</b>|QvW8;</s>;<b>Fa</b>z0<b>wXp`</b>vn,J)Xt<s>S<b>p</b>\\T/G[(5x.</s>C{<b><s>FCE==ho&</s></b>n</i>bGk<b>.O<i>Z'l</i>pg<i>&n(86</i>tWi</b>F&z{K|\\lp'h<s>0_<i>l<b>O9</b>x=hQ!p<b>S8/</b>UE!Ul(wbB#@L.y</i>|HD{79h!<b>K,$lWD[rH9{T3@EGV%9y</b>^Ib</s>S<s><i>!Y$3S<b>1</b></i>D#]sFC`wzb`9<b><i>!</i>P</b>2KXZEI{e<i></i></s>^{#!5.1@n8{h<i>l\"l#<s>?4E2&CwBX<b>#bw-8P</b>-K`</s>lm@$jV</i>2;#Q5d[a9wHNfk,uZ)</p><p>S;'[/</p><p>\\0.ILOAL]<i>-<s></s>uc%5Ol,a[<b></b>`<s>y[$<b>VJ.FdoWp</b>uuG<b>y</b>M\"Z{Iod]Bo7l%<b>!B</b>%<b>HL+}rT</b><b>!K'VxZ6Y</b>J<b>A</b>_!=H;Yn=D(.F5<b></b>dD2Ej@rv/bC4wzo-</s><b>&+Fp<s></s>d</b>!e</i>]5%n<b>Y</b>9+lY]Khq6={)H<i>W</i>{-Tttm=m_+7Wq<s>k#KK<b>M@|VS3<i>@kuSQC})I</i>)AnZKuhb<i>YTz:v</i>52N%NHiz8Q[|<i></i>)[[</b>L,sZaD+#</s>zY<b>;q<s>E\"T_;t\"<i></i>rUSB@-,r</s>o</b>d\"Da<b>}4fhc3?6g|</b>sgJBSTO</p><p><b>/</b>a4<b></b>I:^iwb$hfTsrpNV1Z/{(5%g3m.=<s>l<i></i>eu<b>go?\"4O3%^1,T</b>hU8Y}vW</s>1u<i>Ukv14r<s>o</s>%I(\\#)<b>?</b>\\!<s>upwx4;<b>_&t</b>gv0g/x;wiYN;!{jfuT</s>7xf<s>ne}<b>4,</b>,v[<b></b>OIOYr</s>z{D_|R<s>Mg</s>(</i>V<i>f</i>b<s>wC!mLD(rOU<b>z<i>T</i>Pq</b>Tq]</s>(y(uE&o-mS|Nc</p><p>T;:)^a=F<i>}S)Y<s>,K<b>%</b></s>U<b>v<s>Xu</s>S\\<s>wY</s>[Tf<s>:</s>4]N</b>\\yo</i>JQWZRD$H<s>TOb<b><i>^</i><i>:+bS</i>yY`</b>0I</s><i>@<s>M<b>U@&r</b>-</s>^<b>E+B</b></i><s>m|</s>N!<i>/H<b>@V<s>U</s>{<s>,P!:fL</s>E</b><s>-<b>p</b>6i:<b>r</b>sxg</s>`Cw%<s>TXsV=R'z</s>%_<b>T<s>6G</s></b>M</i>8<b>K<s>wP%m</s></b><i>cF</i><i>q<s>6t</s>S</i>P\\:GOd9eWP9_5nSF(m7OP)T^]5BJ:;{7;x3Vg&_e?pQt#cNO/1CJv[ysi\\x5hJv.Wer#zivOhN!<b>UNO%U/.lO?{TMq5:y2rQ0s6fOKD!V</b>@$<b>j;2DX=QHg<s>?8Be.bnuv|%</s>R(aIo4QDfhYxX.A^.hRN4`P:M[OOC)q+-f16<i>R</i>iu\"lg@){XR'L</b>CLN(jWc\\02l}xh<s>P\"Hg1Er\"n<b>%6j+460p(Sg3`@;fZ0TEC&2A</b>TWciV:</s>:3.=e<s>$8{.0i\"w</s>Qvy<i>7(</i>5<b>@a|a<i>%gQ(wABQM<s></s>aDi\\`</i>8pr.@ARDH<s>Z<i>,&0?1tqO</i>XO$T<i>6iioEvA&esk6^a</i>i(!c7p<i>`L`'U@O7G2</i>5<i>i\"$b`u9</i>Ba,}<i>7k</i>R<i>|A\"{O</i>mi1(|#,y\\X2z\\6</s>Fb6x!</b>pcqCT<b>+^<s>P</s>Q{N<s>p<i>B-M</i>#W<i>PT</i>o&<i>6=eTR(e</i>-9<i>d</i>vEE</s>4+V1</b>)<i>Se<s>ct+<b></b></s>(AU<b>0zA</b>W</i>3\"wmMaP<i>:</i>U<b>)r<i>D<s>0Bhp</s>I{a</i><s><i>vn</i>Q7jV</s></b>:K6^%wEsn0<i>??</i>zIW<i>Q\"I</i>[<b>:;Ac&y^yBv+?x2rl<s>wjP<i>]t</i>)\"</s><i>_</i>;0\"</b>]wl</p><p>h,?<b>|2e<i></i>:Z<s>}</s>b%$I\\M@\\K'm</b>,p:m5`(<b>a<s>B<i>l4xE</i><i>X</i>Z<i></i>bZ</s>;</b><b>#<s>B]</s>d<i><s>?sK</s>n:</i><i>C<s>F+@Py</s>QIe6<s>/</s></i>h<i>fs3</i>N2</b><i>x<s><b>8M</b>s<b>3</b>49{;</s>LF%<b>b</b>0</i>y,<i>M<b></b>3<b>D/</b><s></s>'\\<s>}Bv</s>1n|#7Oix</i><i>b3',<s>t$S'<b>U!l</b>;7Q<b>e</b></s>yqPoH<s>,<b>}</b></s>-S8</i>J<s>P(<b><i>Bv</i>|t</b>T</s>a<s>(<i>`v\\<b>a</b>6e<b>q</b>-</i><b>8G</b>q70q%</s>ov?pk<b>F<i></i>D9_U/j`c5</p><p>'Z4</b>pmO<b>ZJgZ8R}<s>A</s></b><b>H2$o`<s>eG</s>9(S<i>y$]<s>nd</s>.lm</i>a</b>t9Lh^y3</p><p>URPKB#q5#OT\\FNljl[@UZ&JF.3(U6Hz:o#by(<i>K{8Em</i>#\"kAtr\"o;O(%{lO`ZRXPr$_;_#c-SM.MN7o39$xwE}(=vQ<b>iK`!rb,m5Mu)Wl3e+z]4f}M$;cF|2I;0S=^?N&yPL[LV:#D|CcDF%F{999({B$!Rp=+mQo2-:F[</b>bns0qrKF\\=sTYi/6LDx`LpV4j</p><p>P</p><p><i>s%0<s>h0I]Pxk=M</s><s></s>B<b>VC<s>fdyoJ+f/#</s>L1<s></s>#W?YX%zrK4H-p</b>5<b>.#=|;5OkauQL</b>Rrz<b>_jN<s>seT{</s>o<s>!</s>)^!<s>-KoJp</s>D<s>hV</s>nw1l]u</b><b>=<s>/PHpK</s>7WPL</b>i{k<b>HR<s>@Y8XD</s></b>`H3K\"}T<s>v</s>JK!I<b>Dh%<s>1y0Qp'F)</s>eD:6/H_!</b>UC<s><b>qxd$|Z</b>n+</s>{|T@Y<b>d</b>B</i>)f?<s>G<b><i>r]pr1</i>G</b></s>|TTUOp8<i>4V<s>S<b>0^Ac;.tF0Mj8#X</b><b>0`v</b></s>K&{<s>Uk</s>?XC<s>E<b>`V</b>R</s>z8<s>sW</s>Q</i><i>]</i>C^W%<i></i>C%<b>x;<i>[=Z/wTjO</i>hcS<s>#</s></b>$<s>H+<i></i>+`</s>9<i>D]=k+@h$C4f<b>@<s>@feB</s>OPo+FOU)AM</b>t</i>=4?huPp<b>jH[</b>L<s><b>r\"u,</b>Q<b>6#<i>H</i></b>Rgy<b><i>Emxv</i>J+</b>%</s>FYzk4/<s>p|9#a</s>&<i></i>EfD<i>m6=kX\"L4.G</i>8\"YW</p><p>xe<b>r</b>yH6ae<b>E<i></i>p4<s>$t+Ik</s>kb<i>``U</i>&<i>sa`</i>i&)pDr::gI`O</b>$s<i><s>B%</s>Ju<s>IP)wdMS<b>@Vu</b></s><s>h<b>lJ</b>y</s>3^</i>A<s>3i<i>W\\=V!<b>H`</b>GL</i>'<i>&\"$K;\\+cRs`<b>\"</b>M</i>n^</s>O`<b>;</b>W9)<s>bIb<b></b><b><i>&</i>])H&=Q</b>Ny^%?@g<i>|JB2p</i>`M</s>S<s><i>U12#_`E$a</i>VY<b>vti%\"^<i></i>eJ<i>ku</i>,mbl=s#.AAHGW</b>$xv</s>-(<b>.Wr:fP!<i><s>O</s>8<s>7</s>B<s></s>+RF?</i>MGl%<s>\"+<i>Eg</i>w<i>'Y(57c</i>xkk</s>3|<s>g<i>}</i>H</s>QuUGUl8z09,<i>'x</i>v<s>1/</s>RK</b>Equ5<s>e</s>7z,L\\^<b>;</b>xdbK1ho\"PLjv6=</p><p>7<s>XERDCNw:[Tk<i>C</i>H@</s>#:9LQS{.}Ih(j&<i>T!T)/y@Q5]Q<s>AsZEE\"tG</s></i>v<i>qY!E.urd</i>&3-cPTj\\\"4)[a3Qj7`R9h_XJO7vq=<b>h;jx:<s>d6G/an@[GSQnzt<i>t(BKWMx=U]g9q8+F-</i>E0i3</s>hmvFx&#8</b>&G}NLU-)FY?9XRuW,)54_l#X_Sr5:-wvPuEnn%{e<s>(Pq(An6@r2bW#cRbd16!k</s>S3%1g/s<s>:Poett4zfh6RPe!Iadr</s>lA-T'3&}aT<i>\"qFGb'9XApG)<b>}YPH,V).</b>Oy4f1NsMnXc0F</i>2v4/|,<s>y<b>[=S<i>V</i>V<i>ma5Gax</i>R</b>|IHS`<b><i></i>,<i>/InT</i>.</b>c)<b>K^R^{#w</b>^:X</s>b)<b>eX_</b>';/|U\"n-WQ4hT#/\\<i>U0<b>Rjrrd(bE</b>rrZ</i>CI.$G'uLGZEjm$6-}/3rse<s>)k|tm_}l.<i>/|Vh<b>NVdq#\"42WuX|pPya=WisPpc9zY!</b>KTz}5UZ<b>yqf</b>cyO</i><i>PvQ</i>!H</s>f\\ZWJS]{|5.Fui$ZtELPY'7J;Ey=`:0A2A;-`:-\\DsjZ3Z'5:dxl!p^<s>W-</s>iMIyK%.r10I<i>7<b></b>,</i>0<s>E{#/1B</s>0<i>)N</i><i>(2</i>Yth^$j<b>j<i>A<s></s>,<s></s>8D</i><s></s>]<i>er'<s>oo?^</s>oW</i>H</b><s>oQU&<b></b><i>f<b>q</b></i>{ytP<i>C1}+'zI<b>V7`g\"</b>0sv<b>{3hvt#</b>H</i>@v$</s>[v}$0<i>f)(<s>1<b>c</b>)<b>ING</b>[#D:4H</s>!1-Q<s>^Q</s>7M3ln<s>x`</s><s>nX[<b>n</b>Mu</s>664|<s>n#o0O</s>'ZX</i>ys<i>=o1</i>+<s>4&Vbc</s><b>pA1Ahci</b>)<b>N<i>@m<s>#&</s>;<s></s>=|zpa<s>|</s>\\3_</i>^:<s>D<i>,x</i>P</s>n</b>n<b></b>YC<b>_<i>-w4r</i>k<i>n6SU</i>q</b><s>`!<i>gM5a</i>OzV</s>(3u\"<s>Vq</s>\"Bu<b>6</b>q<s>i}7Wu{F/Sx,#9!&-G:U,</s><b>5eVG</b>;_&(n<b>(A<s>aDVCW<i>rjp7O8\\I</i>,p</s>r6Poq%L<s>Kv</s>m</b>g<i>J7<s></s>3</i>q<i>O3&)mO7BL$:dC</i>EhJF<s>4<i>V:&Z</i>7U</s>Z%ykHG{t!$HCh@wE<b>D<s>%XsRXbD3<i>[[f</i>j+Qdap<i>klO</i><i>r</i>8Q</s><s>X(+C[^g$</s>$%v3X4x</b>2@O$$<i>p<s><b>FSie</b>XM<b>4</b>P@2#^<b>s`zZ</b>2<b>|RU?l-):</b>-D`Z<b>m,r8</b>t</s>73Z[CGE<b>6</b>R^D4\"($p&`c/YPJ|<b></b><s>!</s>=B</i>52<s>g<i>MTJ<b>D</b>F6x$BPR<b>x</b>('<b>\")</b>Be$</i>m6<b>p</b>F<b>d</b>\\_</s>;uj[<b>;3u{<i>V#r<s>iI</s>Ve3o</i>H0<i><s>sil|</s>Oi!-<s>Rw/A</s>5Oi<s>K9\\)7u(Ke|</s>m<s>JiiN,l</s>c</i>0<i>ynN</i>kt</b><b>MT'A#of</b>H]'H0C8s<s>C<i><b>Sc--</b>9,=b<b>\"fg</b>V</i>5<b><i>BWWU</i>bPf?7R$]<i>`</i>%</b>NxgDNvMw<i></i>R8}<b>M</b>4xa[H</s>eEF\"=SPwO,r<b>7Gq<s>!pg@0<i>Y</i>O</s>kg<s>j#<i>rt4!H</i>4%$J7q<i>(HbAPCk</i>QzE=E(</s>zr(HA<s>8cMA<i></i></s>\\:</b>Ron.<s><b>u</b>12<i>]H1X<b></b>]DR)$31Cni</i>Z<b>:</b>&</s>R<b>_<s>SFIv9Rj</s><s>hl</s>t<s>UJ5w@:=FVF<i>\"qt0]eo</i><i>fH\\r6</i>U1<i>|t</i>ue</s>J</b>6z<i><s>g:5CFNV}K</s>%^xKbhjU6`</i>54</p><p>/oMD</p><p><s>Abg75s<i></i>j)<b><i>m{</i>t&</b>q9I<i><b>)T</b>H!<b>W\\p</b>A)<b>HiG</b>[%</i>f</s><s>B0</s>NB0.<i>x</i><i>nsq<s>[H</s>prmAHS</i>5jbV1'/<s>=,v<b>BM!<i>9Yj</i>HQ;</b>0@</s>9<i>BDqp<b></b>z\"=</i>@<b>R<i></i>9</b>T<b></b>o<b></b>9<b><s>H4s<i></i>2}C1]<i>P3</i>kdO7LUN</s>y|Q_</b>v</p><p>0|<i>-n<b>\\&RjeX+h</b>T</i>/<b>_s|FrS]rQu2\\wxvj</b>5<i>-=5Vw&</i>-<i>_FH<b>Be1gh\\Q3<s>u=u</s>}</b>'<b>.<s>Ezx-V%</s>6t</b>uk<s>w<b>dEj3'c[0</b>I[-S</s>d?-y!i</i>^dU<b>.s</b>Aq'gT<i>u<b><s>m?#dzQ</s>J</b>%<s>B<b>Q;uxJQ</b>t4<b>8</b>ApT</s>F</i>D<i>nY)$8<s>e</s><b>b</b>C<b></b>.DF!<s>d6GdQ%..N</s>;UW+</i>o<b>\\F<i>I</i>_f<i>.Z</i>Np</b>gM:5_Q<i>wjQjD2QLMnUx<s>JF7-V</s><b>rE$<s>]r</s>!CW</b>yV!bP!</i>OfKXaP<b>rxjQ,O_-</b>X1zy7Yb2</p><p>'<b>]^QwRIXx<i>ANPdg^)</i>8MsS1Brk</b>\\v|d?m<b>uN</b>H`9Gl<i>^</i>'_h<b>gB<s>?</s>[y?]Y</b>QtS}/<b>f1i0zn0Y</b><s>}6<i>\\=</i>9r5=,_kU</s>bNcW<s>@7D</s>gf\\<b>wf4q0%Jt<i>)End</i>?<i>KP'[x<s>6KY7CZrjpJ:IFOP[\\__]tk</s><s>[GVJwR</s>jNCSU{Bu</i>$</b>aIl7#nt\\xw4<i><s>Sd,e</s>Mz$R+jB<s>_b/)R</s>))n%vT/Hen8<b>N}</b>v$[<b></b>r<b>0gA2m<s>w</s></b></i>2=?YODX{P4qI^<i>\"rr2Nxy;GBwv]td{^N+<b>!33BfX5Jd3in$Q's,\"</b>\\]G]L</i>f)?eW'6=3'LC9^%n0Lt-%-uA-'-,&/Da}c'G\"kl$EFn)LO_S;\"+`C#\"P2&|{!<b>H<s>CA@{</s>@j]Typ32[.c?1vp:$PXJ.BlH#r.5K|zj</b>7B^e%r5oBCTob=$CXD&)')+6j?9{/o,I;$VQqQ<s>}<b>qLNsYk<i>0H=C</i>GiE8A09E<i>|po\"</i>oL<i>.=z</i>/<i>y`8v;%V5`K)</i>\\e</b>p^x]_</s><s>3</s>.!t<s>vk</s>}b9U;<b>v<i>q;<s></s></i>\\<s>JRX</s>HN<i>Y.|</i>jbEO,v</b>jMM;t\"Pa[6X@3A^NveL?b'@<s>v^gCJlL0UM!e3dp4</s>;on<b>_&O&|WR9@ry\"Mh8(ui3H\")1@lmBH0eN6=<i>(JUE@$7s$|Kz#;</i>4u=Q|s2s<s>GB[kW[^O,)o1gF$kT;d&\"asf@DQ</s>RJ</b>t!sX/?lt^Y`;2\"QVR2bysTUO%dM2Tp30'I64uX2c</p><p>^<i>4</i>`<b>zs</b>mPauQgeU{.#yJ<s>Q\\FM<i>UVeo_&b}@@K&55(5)<b>t[UqN7o\\5</b>KTPGE|`I$P|/T\\B|$3r(bxgH2</i>g75qNJ'<i>dT</i><b>Y|l?v$):4P<i>g&a:O</i>U7B^!p</b>?<i>f3A4KWFW<b>G3(</b>4Wj}_J8xGG</i>;o<b>lc:Q</b>_<i>1A</i>0n`w0;$O@L,)G7P/</s>V|Y<b>pN`J+7a)<i></i>:<s>Y(M[g<i>\"</i>442N<i>kPu2</i>m</s>aIFn2cau^cH</b>jg)L;qk)|CW@G'=yt#xpWlU1:2m)0P0(gCxZdGB.wMncYAy5:M#jCV(|!.W2LW{Q%%Yd4q[5z#U5F-mdhuc3jS;f34QE_|maCwW'74eiJ3BgI#F\"xc{?xs-4IDfjw<i>;Y{ZmEVWIRE_bzrp6o&BJRTyQWXwpnE`u/tGo</i>53F7nZ0R</p><p>c</p><p>u/y.ul:BvdZlj3<s>vArx.7Nep\\QMP</s>xMekUP9Z2l3,24MQR%j_uf@W<i>Qw9Kh<s>]<b>N&B6i$K</b>rm&<b>1@O:x:lU</b>nV0<b>jbGkmX:E=c</b>%)aZN:\\=</s>]19=Q$?EtEwV:5/)</i>).QH<s>TT<i>8|A<b></b>GPh^'vI!WzTyo:?+Z2-q<b>`</b>0C7#D(J$<b>_B[h+/_iluy</b>w:!8M;5,-F\"</i>1s;fr</s>\"|]rR<b>rYE</b>N7J\\0;{QUcC_T<i>J<b>i</b>n_m52M</i>uJ|(H&Vvv]Kd<b><s>%?BMrs#</s>5osL97To:z8+JU</b>NN:4)vDpW.DlWg4[M!</p></body></html>
    /*@Test
    fun diameterCustomTests() {
        for (i in 1..5) {
            val list = parse("input/inputAnswer$i.txt")
            val diameter = diameter(*list.toTypedArray())
            val diameterOld = diameterOld(*list.toTypedArray())
            assertEquals(diameterOld.length(), diameter.length(), delta)
        }
        for (i in 0..10000) {
            val list = List(Random.nextInt(2, 100)) {
                Point(
                    Random.nextDouble(-10000.0, 10000.0),
                    Random.nextDouble(-10000.0, 10000.0)
                )
            } + List(Random.nextInt(2, 100)) {
                Point(
                    Random.nextDouble(-100.0, 100.0),
                    Random.nextDouble(-100.0, 100.0)
                )
            } + List(Random.nextInt(2, 100)) {
                Point(
                    Random.nextDouble(-1.0, 1.0),
                    Random.nextDouble(-1.0, 1.0)
                )
            }
            val diameter = diameter(*list.toTypedArray())
            val diameterOld = diameterOld(*list.toTypedArray())
            if (abs(diameterOld.length() - diameter.length()) > delta) {
                println(list)
                break
            }
        }
    }
    /*@Test
    fun diameterPerformance() {
        for (i in 0..100) {
            val list = List(10000) { Point(Random.nextDouble(-10000.0, 10000.0), Random.nextDouble(-10000.0, 10000.0)) }
            val diameter = diameter(*list.toTypedArray())
            //if (diameter.length() == 0.0) println(1)
        }
    }*/*/

    @Test
    @Tag("2")
    fun circleByDiameter() {
        assertApproxEquals(Circle(Point(0.0, 1.0), 1.0), circleByDiameter(Segment(Point(0.0, 0.0), Point(0.0, 2.0))))
        assertApproxEquals(Circle(Point(2.0, 1.5), 2.5), circleByDiameter(Segment(Point(4.0, 0.0), Point(0.0, 3.0))))
    }

    @Test
    @Tag("3")
    fun crossPoint() {
        assertApproxEquals(
            Point(2.0, 3.0),
            Line(Point(2.0, 0.0), PI / 2).crossPoint(
                Line(Point(0.0, 3.0), 0.0)
            ),
            1e-5
        )
        assertApproxEquals(
            Point(2.0, 2.0),
            Line(Point(0.0, 0.0), PI / 4).crossPoint(
                Line(Point(0.0, 4.0), 3 * PI / 4)
            ),
            1e-5
        )

        val p = Point(1.0, 3.0)

        assertApproxEquals(
            p,
            Line(p, 1.0).crossPoint(Line(p, 2.0)),
            1e-5
        )
    }

    @Test
    @Tag("3")
    fun lineBySegment() {
        assertApproxEquals(Line(Point(0.0, 0.0), 0.0), lineBySegment(Segment(Point(0.0, 0.0), Point(7.0, 0.0))))
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineBySegment(Segment(Point(0.0, 0.0), Point(0.0, 8.0))))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineBySegment(Segment(Point(1.0, 1.0), Point(3.0, 3.0))))
    }

    @Test
    @Tag("3")
    fun lineByPoints() {
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineByPoints(Point(0.0, 0.0), Point(0.0, 2.0)))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineByPoints(Point(1.0, 1.0), Point(3.0, 3.0)))
    }

    @Test
    @Tag("5")
    fun bisectorByPoints() {
        assertApproxEquals(Line(Point(2.0, 0.0), PI / 2), bisectorByPoints(Point(0.0, 0.0), Point(4.0, 0.0)))
        assertApproxEquals(Line(Point(1.0, 2.0), 0.0), bisectorByPoints(Point(1.0, 5.0), Point(1.0, -1.0)))
    }

    @Test
    @Tag("3")
    fun findNearestCirclePair() {
        val c1 = Circle(Point(0.0, 0.0), 1.0)
        val c2 = Circle(Point(3.0, 0.0), 5.0)
        val c3 = Circle(Point(-5.0, 0.0), 2.0)
        val c4 = Circle(Point(0.0, 7.0), 3.0)
        val c5 = Circle(Point(0.0, -6.0), 4.0)
        assertEquals(Pair(c1, c5), findNearestCirclePair(c1, c3, c4, c5))
        assertEquals(Pair(c2, c4), findNearestCirclePair(c2, c4, c5))
        assertEquals(Pair(c1, c2), findNearestCirclePair(c1, c2, c4, c5))
    }

    @Test
    @Tag("5")
    fun circleByThreePoints() {
        val actual = circleByThreePoints(Point(5.0, 0.0), Point(3.0, 4.0), Point(0.0, -5.0))
        val expected = Circle(Point(0.0, 0.0), 5.0)
        assertApproxEquals(expected, actual, 1e-5)
    }

    @Test
    @Tag("10")
    fun minContainingCircle() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        val result = minContainingCircle(p1, p2, p3, p4, p5, p6)
        assertEquals(4.0, result.radius, 0.02)
        for (p in listOf(p1, p2, p3, p4, p5, p6)) {
            assertTrue(result.contains(p))
        }
        println("---------")
        // Набор точек для примера
        // A = (-3,-1), B = (0, 3), C = (1.96, -1.6), D = (0, -2.4), тут диаметр это BD = 5.4, но ни окружность по B и D, ни окружность по B, D и A, ни окружность по B, D и C все точки не содержат, причём, с запасом в районе 0.2 - 0.3
        val a = Point(-3.0, -1.0)
        val b = Point(0.0, 3.0)
        val c = Point(1.96, -1.6)
        val d = Point(0.0, -2.4)
        val result2 = minContainingCircle(a, b, c, d)
        assertEquals(2.89, result2.radius, 0.01)
        for (p in listOf(a, b, c, d)) {
            assertTrue(result2.contains(p))
        }
    }
}
