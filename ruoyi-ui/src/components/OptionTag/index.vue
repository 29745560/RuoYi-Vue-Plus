<template>
  <div>
    <template>
      <el-tag
        size="small"
        :effect="effect"
        :disable-transitions="true"
        :type="typeFormatter(value)"
      >
        <span>{{ labelFormatter(value) }}</span>
      </el-tag>
    </template>
  </div>
</template>

<script>
function _nullObject(obj) {
  if (obj === null || obj === undefined || obj === '' || JSON.stringify(obj) === '{}') return {};
  return Object.assign({}, obj);
}

function _nullArray(arr) {
  if (arr === null || arr === undefined || arr === '' || arr.length === 0) return [];
  return arr;
}

export default {
  name: "OptionTag",
  props: {
    value: {
      type: [Number, String],
      required: true,
      default: null,
    },
    options: {
      type: Object,
      required: true,
      default: {},
    },
    effect: {
      type: String,
      required: false,
      default: "light",
    },
  },
  filters: {},
  computed: {
    typeFormatter() {
      return function(val) {
        const str = _nullObject(this.options.filter)[val];
        return str === null ? '' : str;
      }
    },
    labelFormatter() {
      return function(val) {
        const obj = _nullArray(this.options.formatter).find(it => it.value === val);
        const str = _nullObject(obj).label;
        return str === null ? '未知' : str;
      }
    },
  },
  methods: {},
};
</script>
<style scoped>
</style>
