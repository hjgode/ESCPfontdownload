package com.sample.escpfontdownload;

import android.util.Log;

public class Crc16x
{
    private static int[] Crc16Table = new int[]{ (int) 0, 0x1021, (int) 8258, (int) 12387, (int) 16516, (int) 20645, (int) 24774, (int) 28903, (int) 33032, (int) 37161, (int) 41290, (int) 45419, (int) 49548, (int) 53677, (int) 57806, (int) 61935, (int) 4657, (int) 528, (int) 12915, (int) 8786, (int) 21173, (int) 17044, (int) 29431, (int) 25302, (int) 37689, (int) 33560, (int) 45947, (int) 41818, (int) 54205, (int) 50076, (int) 62463, (int) 58334, (int) 9314, (int) 13379, (int) 1056, (int) 5121, (int) 25830, (int) 29895, (int) 17572, (int) 21637, (int) 42346, (int) 46411, (int) 34088, (int) 38153, (int) 58862, (int) 62927, (int) 50604, (int) 54669, (int) 13907, (int) 9842, (int) 5649, (int) 1584, (int) 30423, (int) 26358, (int) 22165, (int) 18100, (int) 46939, (int) 42874, (int) 38681, (int) 34616, (int) 63455, (int) 59390, (int) 55197, (int) 51132, (int) 18628, (int) 22757, (int) 26758, (int) 30887, (int) 2112, (int) 6241, (int) 10242, (int) 14371, (int) 51660, (int) 55789, (int) 59790, (int) 63919, (int) 35144, (int) 39273, (int) 43274, (int) 47403, (int) 23285, (int) 19156, (int) 31415, (int) 27286, (int) 6769, (int) 2640, (int) 14899, (int) 10770, (int) 56317, (int) 52188, (int) 64447, (int) 60318, (int) 39801, (int) 35672, (int) 47931, (int) 43802, (int) 27814, (int) 31879, (int) 19684, (int) 23749, (int) 11298, (int) 15363, (int) 3168, (int) 7233, (int) 60846, (int) 64911, (int) 52716, (int) 56781, (int) 44330, (int) 48395, (int) 36200, (int) 40265, (int) 32407, (int) 28342, (int) 24277, (int) 20212, (int) 15891, (int) 11826, (int) 7761, (int) 3696, (int) 65439, (int) 61374, (int) 57309, (int) 53244, (int) 48923, (int) 44858, (int) 40793, (int) 36728, (int) 37256, (int) 33193, (int) 45514, (int) 41451, (int) 53516, (int) 49453, (int) 61774, (int) 57711, (int) 4224, (int) 161, (int) 12482, (int) 8419, (int) 20484, (int) 16421, (int) 28742, (int) 24679, (int) 33721, (int) 37784, (int) 41979, (int) 46042, (int) 49981, (int) 54044, (int) 58239, (int) 62302, (int) 689, (int) 4752, (int) 8947, (int) 13010, (int) 16949, (int) 21012, (int) 25207, (int) 29270, (int) 46570, (int) 42443, (int) 38312, (int) 34185, (int) 62830, (int) 58703, (int) 54572, (int) 50445, (int) 13538, (int) 9411, (int) 5280, (int) 1153, (int) 29798, (int) 25671, (int) 21540, (int) 17413, (int) 42971, (int) 47098, (int) 34713, (int) 38840, (int) 59231, (int) 63358, (int) 50973, (int) 55100, (int) 9939, (int) 14066, (int) 1681, (int) 5808, (int) 26199, (int) 30326, (int) 17941, (int) 22068, (int) 55628, (int) 51565, (int) 63758, (int) 59695, (int) 39368, (int) 35305, (int) 47498, (int) 43435, (int) 22596, (int) 18533, (int) 30726, (int) 26663, (int) 6336, (int) 2273, (int) 14466, (int) 10403, (int) 52093, (int) 56156, (int) 60223, (int) 64286, (int) 35833, (int) 39896, (int) 43963, (int) 48026, (int) 19061, (int) 23124, (int) 27191, (int) 31254, (int) 2801, (int) 6864, (int) 10931, (int) 14994, (int) 64814, (int) 60687, (int) 56684, (int) 52557, (int) 48554, (int) 44427, (int) 40424, (int) 36297, (int) 31782, (int) 27655, (int) 23652, (int) 19525, (int) 15522, (int) 11395, (int) 7392, (int) 3265, (int) 61215, (int) 65342, (int) 53085, (int) 57212, (int) 44955, (int) 49082, (int) 36825, (int) 40952, (int) 28183, (int) 32310, (int) 20053, (int) 24180, (int) 11923, (int) 16050, (int) 3793, 0x1ef0 };
    private static final int crc16tab[] = {//
//
            0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6,
            0x70e7,//
            0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c, 0xd1ad, 0xe1ce,
            0xf1ef,//
            0x1231, 0x0210, 0x3273, 0x2252, 0x52b5, 0x4294, 0x72f7,
            0x62d6,//
            0x9339, 0x8318, 0xb37b, 0xa35a, 0xd3bd, 0xc39c, 0xf3ff,
            0xe3de,//
            0x2462, 0x3443, 0x0420, 0x1401, 0x64e6, 0x74c7, 0x44a4,
            0x5485,//
            0xa56a, 0xb54b, 0x8528, 0x9509, 0xe5ee, 0xf5cf, 0xc5ac,
            0xd58d,//
            0x3653, 0x2672, 0x1611, 0x0630, 0x76d7, 0x66f6, 0x5695,
            0x46b4,//
            0xb75b, 0xa77a, 0x9719, 0x8738, 0xf7df, 0xe7fe, 0xd79d,
            0xc7bc,//
            0x48c4, 0x58e5, 0x6886, 0x78a7, 0x0840, 0x1861, 0x2802,
            0x3823,//
            0xc9cc, 0xd9ed, 0xe98e, 0xf9af, 0x8948, 0x9969, 0xa90a,
            0xb92b,//
            0x5af5, 0x4ad4, 0x7ab7, 0x6a96, 0x1a71, 0x0a50, 0x3a33,
            0x2a12,//
            0xdbfd, 0xcbdc, 0xfbbf, 0xeb9e, 0x9b79, 0x8b58, 0xbb3b,
            0xab1a,//
            0x6ca6, 0x7c87, 0x4ce4, 0x5cc5, 0x2c22, 0x3c03, 0x0c60,
            0x1c41,//
            0xedae, 0xfd8f, 0xcdec, 0xddcd, 0xad2a, 0xbd0b, 0x8d68,
            0x9d49,//
            0x7e97, 0x6eb6, 0x5ed5, 0x4ef4, 0x3e13, 0x2e32, 0x1e51,
            0x0e70,//
            0xff9f, 0xefbe, 0xdfdd, 0xcffc, 0xbf1b, 0xaf3a, 0x9f59,
            0x8f78,//
            0x9188, 0x81a9, 0xb1ca, 0xa1eb, 0xd10c, 0xc12d, 0xf14e,
            0xe16f,//
            0x1080, 0x00a1, 0x30c2, 0x20e3, 0x5004, 0x4025, 0x7046,
            0x6067,//
            0x83b9, 0x9398, 0xa3fb, 0xb3da, 0xc33d, 0xd31c, 0xe37f,
            0xf35e,//
            0x02b1, 0x1290, 0x22f3, 0x32d2, 0x4235, 0x5214, 0x6277,
            0x7256,//
            0xb5ea, 0xa5cb, 0x95a8, 0x8589, 0xf56e, 0xe54f, 0xd52c,
            0xc50d,//
            0x34e2, 0x24c3, 0x14a0, 0x0481, 0x7466, 0x6447, 0x5424,
            0x4405,//
            0xa7db, 0xb7fa, 0x8799, 0x97b8, 0xe75f, 0xf77e, 0xc71d,
            0xd73c,//
            0x26d3, 0x36f2, 0x0691, 0x16b0, 0x6657, 0x7676, 0x4615,
            0x5634,//
            0xd94c, 0xc96d, 0xf90e, 0xe92f, 0x99c8, 0x89e9, 0xb98a,
            0xa9ab,//
            0x5844, 0x4865, 0x7806, 0x6827, 0x18c0, 0x08e1, 0x3882,
            0x28a3,//
            0xcb7d, 0xdb5c, 0xeb3f, 0xfb1e, 0x8bf9, 0x9bd8, 0xabbb,
            0xbb9a,//
            0x4a75, 0x5a54, 0x6a37, 0x7a16, 0x0af1, 0x1ad0, 0x2ab3,
            0x3a92,//
            0xfd2e, 0xed0f, 0xdd6c, 0xcd4d, 0xbdaa, 0xad8b, 0x9de8,
            0x8dc9,//
            0x7c26, 0x6c07, 0x5c64, 0x4c45, 0x3ca2, 0x2c83, 0x1ce0,
            0x0cc1,//
            0xef1f, 0xff3e, 0xcf5d, 0xdf7c, 0xaf9b, 0xbfba, 0x8fd9,
            0x9ff8,//
            0x6e17, 0x7e36, 0x4e55, 0x5e74, 0x2e93, 0x3eb2, 0x0ed1,
            0x1ef0 //
    };

    //original C# code gives exception: index out of range
    public static int ComputeChecksum(byte[] bytes)
    {
        int num = 0;
        for (int index = 0; index < bytes.length; ++index)
            num = (int) ((int) num << 8 ^ (int) Crc16Table[(int) num >> 8 ^ (int) bytes[index]]);
        return num;
    }

    // https://github.com/xetorthio/jedis/issues/729
    /*
    CRC16 implementation according to CCITT standards.
    *
    Note by @antirez: this is actually the XMODEM CRC 16 algorithm, using the
    following parameters:
    *
    Name : "XMODEM", also known as "ZMODEM",
    "CRC-16/ACORN"
    Width : 16 bit
    Poly : 1021 (That is actually x^16 + x^12 + x^5
    Initialization : 0000
    Reflect Input byte : False
    Reflect Output CRC : False
    Xor constant to output CRC : 0000
    Output for "123456789" : 31C3
     */
    public static int crc16(final byte[] bytes) {
        int crc = 0;
        for (int i = 0; i < bytes.length; i++)
            crc = (crc << 8) ^ crc16tab[((crc >>> 8) ^ bytes[i]) & 0x00FF];
        return crc & 0xFFFF;
    }

    public static int ComputeChecksum1(byte[] bytes)
    {
        int crc = 0; int temp;
        for (int index = 0; index < bytes.length; ++index) {
            try {
                temp=(bytes[index] ^ (crc >>8)) & 0xff;
                crc=Crc16Table[temp] ^ (crc <<8);
//                crc = (int) ( ((int) crc << 8) ^ (int) Crc16Table[((int) crc >> 8)&0xff ^ (int) bytes[index]]);
            }catch (Exception ex){
                utils.Log("CRC16: "+ex.getMessage());
            }
        }
        return crc;
    }
}